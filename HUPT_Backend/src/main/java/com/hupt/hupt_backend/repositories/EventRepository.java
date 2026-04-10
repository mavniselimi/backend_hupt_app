package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCreatedBy(User user);

    // Find events where the user has a registration entry
    @Query("SELECT r.event FROM EventRegistration r WHERE r.user = :user")
    List<Event> findByRegisteredUser(@Param("user") User user);

    /**
     * Pessimistic write lock on the event row.
     * Used during registration to serialize queue number assignment:
     * all concurrent registrations for the same event compete for this lock,
     * so only one proceeds at a time — preventing duplicate queue numbers.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);
}
