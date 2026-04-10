package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.EventRegistration;
import com.hupt.hupt_backend.entities.EventRegistrationStatus;
import com.hupt.hupt_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    // ── Full event queue (all desks, ordered by desk then queue position) ─────────
    List<EventRegistration> findByEventOrderByAssignedRegistrarIdAscQueueNumberAsc(Event event);

    // ── Single desk queue ─────────────────────────────────────────────────────────
    List<EventRegistration> findByEventAndAssignedRegistrarOrderByQueueNumberAsc(Event event, User registrar);

    List<EventRegistration> findByEventAndAssignedRegistrarAndStatusOrderByQueueNumberAsc(
            Event event, User registrar, EventRegistrationStatus status);

    // ── Existence checks ──────────────────────────────────────────────────────────
    Optional<EventRegistration> findByEventAndUser(Event event, User user);

    boolean existsByEventAndUser(Event event, User user);

    // ── Per-desk queue number assignment ─────────────────────────────────────────
    // Max queue number for a specific registrar within a specific event
    // Used to assign the next queue number when a new user is routed to this desk
    @Query("SELECT COALESCE(MAX(r.queueNumber), 0) FROM EventRegistration r " +
           "WHERE r.event = :event AND r.assignedRegistrar = :registrar")
    int findMaxQueueNumberByEventAndRegistrar(@Param("event") Event event,
                                              @Param("registrar") User registrar);

    // ── Load-balancing: count PENDING registrations per active registrar ──────────
    // Returns [registrarId, pendingCount] pairs for all active registrars of this event
    // The service uses this to pick the least-loaded desk
    @Query("SELECT r.assignedRegistrar.id, COUNT(r) FROM EventRegistration r " +
           "WHERE r.event = :event AND r.status = 'PENDING' " +
           "GROUP BY r.assignedRegistrar.id")
    List<Object[]> countPendingPerRegistrarForEvent(@Param("event") Event event);

    // ── User's own registrations ──────────────────────────────────────────────────
    List<EventRegistration> findByUser(User user);

    List<EventRegistration> findByUserAndStatus(User user, EventRegistrationStatus status);
}
