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

    List<EventRegistration> findByEventOrderByQueueNumberAsc(Event event);

    List<EventRegistration> findByEventAndStatusOrderByQueueNumberAsc(Event event, EventRegistrationStatus status);

    Optional<EventRegistration> findByEventAndUser(Event event, User user);

    boolean existsByEventAndUser(Event event, User user);

    // Get the highest queue number for this event so we can assign the next one
    @Query("SELECT COALESCE(MAX(r.queueNumber), 0) FROM EventRegistration r WHERE r.event = :event")
    int findMaxQueueNumberByEvent(@Param("event") Event event);

    // All events a user is registered for
    List<EventRegistration> findByUser(User user);

    // All events a user registered (by status)
    List<EventRegistration> findByUserAndStatus(User user, EventRegistrationStatus status);
}
