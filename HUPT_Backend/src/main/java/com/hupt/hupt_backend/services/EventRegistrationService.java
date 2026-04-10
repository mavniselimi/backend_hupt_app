package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.*;
import com.hupt.hupt_backend.repositories.EventRegistrationRepository;
import com.hupt.hupt_backend.repositories.EventRepository;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    /**
     * Register a user for an event.
     * Assigns the next queue number automatically.
     * Throws if the user is already registered.
     */
    @Transactional
    public EventRegistration registerUser(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (registrationRepository.existsByEventAndUser(event, user)) {
            throw new RuntimeException("User is already registered for this event");
        }

        int nextQueueNumber = registrationRepository.findMaxQueueNumberByEvent(event) + 1;

        EventRegistration registration = new EventRegistration();
        registration.setEvent(event);
        registration.setUser(user);
        registration.setQueueNumber(nextQueueNumber);
        registration.setStatus(EventRegistrationStatus.PENDING);

        return registrationRepository.save(registration);
    }

    /**
     * Remove a user's registration from an event.
     */
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

    /**
     * Get the full registration queue for an event, ordered by queue number.
     * Used by the Registrar/Admin panel.
     */
    public List<EventRegistration> getQueueForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return registrationRepository.findByEventOrderByQueueNumberAsc(event);
    }

    /**
     * Get only PENDING registrations for an event, ordered by queue number.
     */
    public List<EventRegistration> getPendingQueueForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return registrationRepository.findByEventAndStatusOrderByQueueNumberAsc(
                event, EventRegistrationStatus.PENDING);
    }

    /**
     * Get the next PENDING person in the queue — the one the Registrar should call up next.
     */
    public Optional<EventRegistration> getNextInQueue(Long eventId) {
        List<EventRegistration> pending = getPendingQueueForEvent(eventId);
        return pending.isEmpty() ? Optional.empty() : Optional.of(pending.get(0));
    }

    /**
     * Mark a registration as CARD_ISSUED.
     * Records which registrar processed it and the timestamp.
     * Only Registrar or Admin should be allowed to call this.
     */
    @Transactional
    public EventRegistration issueCard(Long registrationId, Long registrarUserId) {
        EventRegistration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));

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

    /**
     * Get the current user's registration for a specific event.
     */
    public Optional<EventRegistration> getMyRegistration(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return registrationRepository.findByEventAndUser(event, user);
    }

    /**
     * Get all events the user has registered for.
     */
    public List<EventRegistration> getRegistrationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return registrationRepository.findByUser(user);
    }
}
