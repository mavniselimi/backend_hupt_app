package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.*;
import com.hupt.hupt_backend.repositories.EventRegistrationRepository;
import com.hupt.hupt_backend.repositories.EventRepository;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventRegistrationService(EventRegistrationRepository registrationRepository,
                                    EventRepository eventRepository,
                                    UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // REGISTRATION
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Register a user for an event.
     *
     * Automatically routes the user to the least-loaded active desk:
     *   1. Get all registrars assigned to this event
     *   2. Filter to only those with isActive = true
     *   3. Find who has the fewest PENDING registrations
     *   4. If tied, pick randomly
     *   5. Assign the next queue number in that desk's own queue
     *
     * Uses a pessimistic write lock on the event row to serialize concurrent
     * registrations and prevent duplicate (desk, queueNumber) assignments.
     * The unique constraint on (event_id, assigned_registrar_id, queue_number) is the DB safety net.
     */
    @Transactional
    public EventRegistration registerUser(Long eventId, Long userId) {
        // Lock the event row — serializes all concurrent registrations for the same event
        Event event = eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (registrationRepository.existsByEventAndUser(event, user)) {
            throw new RuntimeException("User is already registered for this event");
        }

        // Find active registrars assigned to this event
        List<User> activeRegistrars = event.getAssignedRegistrars().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .collect(Collectors.toList());

        if (activeRegistrars.isEmpty()) {
            throw new RuntimeException(
                    "No active registrars are assigned to this event. " +
                    "An admin must assign at least one active registrar before users can register.");
        }

        // Pick the least-loaded desk (with random tie-breaking)
        User assignedRegistrar = pickLeastLoadedRegistrar(event, activeRegistrars);

        // Next queue number in that desk's own sequence
        int nextQueueNumber = registrationRepository
                .findMaxQueueNumberByEventAndRegistrar(event, assignedRegistrar) + 1;

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setAssignedRegistrar(assignedRegistrar);
        registration.setQueueNumber(nextQueueNumber);
        registration.setStatus(EventRegistrationStatus.PENDING);

        return registrationRepository.save(registration);
    }

    /**
     * Picks the active registrar with the fewest PENDING registrations for this event.
     * Ties are broken randomly so load distributes evenly over time.
     */
    private User pickLeastLoadedRegistrar(Event event, List<User> activeRegistrars) {
        // Build a map: registrarId → pendingCount (only for registrars that already have entries)
        Map<Long, Long> pendingCounts = new HashMap<>();
        List<Object[]> rows = registrationRepository.countPendingPerRegistrarForEvent(event);
        for (Object[] row : rows) {
            Long registrarId = (Long) row[0];
            Long count = (Long) row[1];
            pendingCounts.put(registrarId, count);
        }

        // For registrars with no entries yet, their count is 0
        long minPending = activeRegistrars.stream()
                .mapToLong(r -> pendingCounts.getOrDefault(r.getId(), 0L))
                .min()
                .orElse(0L);

        // Collect all desks tied at the minimum
        List<User> leastLoaded = activeRegistrars.stream()
                .filter(r -> pendingCounts.getOrDefault(r.getId(), 0L) == minPending)
                .collect(Collectors.toList());

        if (leastLoaded.size() == 1) {
            return leastLoaded.get(0);
        }

        // Random tie-breaking
        return leastLoaded.get(new Random().nextInt(leastLoaded.size()));
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // UNREGISTER
    // ══════════════════════════════════════════════════════════════════════════════

    @Transactional
    public void unregisterUser(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        EventRegistration registration = registrationRepository.findByEventAndUser(event, user)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // QUEUE VIEWS
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Full event queue across all desks (admin view).
     * Ordered by desk, then by queue number within each desk.
     */
    public List<EventRegistration> getFullQueueForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return registrationRepository.findByEventOrderByAssignedRegistrarIdAscQueueNumberAsc(event);
    }

    /**
     * Queue for a specific registrar's desk (registrar's own view).
     */
    public List<EventRegistration> getQueueForRegistrar(Long eventId, Long registrarId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        User registrar = userRepository.findById(registrarId)
                .orElseThrow(() -> new RuntimeException("Registrar not found with id: " + registrarId));
        return registrationRepository.findByEventAndAssignedRegistrarOrderByQueueNumberAsc(event, registrar);
    }

    /**
     * Only PENDING entries for a specific desk (the registrar's active work queue).
     */
    public List<EventRegistration> getPendingQueueForRegistrar(Long eventId, Long registrarId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        User registrar = userRepository.findById(registrarId)
                .orElseThrow(() -> new RuntimeException("Registrar not found with id: " + registrarId));
        return registrationRepository.findByEventAndAssignedRegistrarAndStatusOrderByQueueNumberAsc(
                event, registrar, EventRegistrationStatus.PENDING);
    }

    /**
     * Next PENDING person in a specific desk's queue.
     */
    public Optional<EventRegistration> getNextInQueueForRegistrar(Long eventId, Long registrarId) {
        List<EventRegistration> pending = getPendingQueueForRegistrar(eventId, registrarId);
        return pending.isEmpty() ? Optional.empty() : Optional.of(pending.get(0));
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // CARD ISSUANCE
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Mark a registration as CARD_ISSUED.
     * Validates the registration belongs to the given event (prevents cross-event abuse).
     */
    @Transactional
    public EventRegistration issueCard(Long eventId, Long registrationId, Long registrarUserId) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));

        if (!registration.getEvent().getId().equals(eventId)) {
            throw new RuntimeException(
                    "Registration " + registrationId + " does not belong to event " + eventId);
        }

        if (registration.getStatus() == EventRegistrationStatus.CARD_ISSUED) {
            throw new RuntimeException("Card has already been issued for this registration");
        }

        User registrar = userRepository.findById(registrarUserId)
                .orElseThrow(() -> new RuntimeException("Registrar user not found"));

        registration.setStatus(EventRegistrationStatus.CARD_ISSUED);
        registration.setProcessedBy(registrar);
        registration.setCardIssuedAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // USER'S OWN REGISTRATION
    // ══════════════════════════════════════════════════════════════════════════════

    public Optional<EventRegistration> getMyRegistration(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return registrationRepository.findByEventAndUser(event, user);
    }

    public List<EventRegistration> getRegistrationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return registrationRepository.findByUser(user);
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // REGISTRAR ASSIGNMENT TO EVENT
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Assign a registrar to an event (admin action).
     * Only users with the Registrar role can be assigned.
     */
    @Transactional
    public Event assignRegistrarToEvent(Long eventId, Long registrarId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        User registrar = userRepository.findById(registrarId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + registrarId));

        if (registrar.getRole() != UserType.Registrar) {
            throw new RuntimeException("User " + registrarId + " does not have the Registrar role");
        }

        if (!event.getAssignedRegistrars().contains(registrar)) {
            event.getAssignedRegistrars().add(registrar);
            eventRepository.save(event);
        }

        return event;
    }

    /**
     * Remove a registrar from an event (admin action).
     * Does NOT delete their existing registrations — those users stay with this desk.
     */
    @Transactional
    public Event removeRegistrarFromEvent(Long eventId, Long registrarId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        User registrar = userRepository.findById(registrarId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + registrarId));

        event.getAssignedRegistrars().remove(registrar);
        return eventRepository.save(event);
    }

    /**
     * Get all registrars assigned to an event.
     */
    public List<User> getRegistrarsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return event.getAssignedRegistrars();
    }
}
