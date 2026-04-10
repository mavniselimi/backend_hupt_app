package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCreatedBy(User user);

    // Find events where the user has a registration entry
    @Query("SELECT r.event FROM EventRegistration r WHERE r.user = :user")
    List<Event> findByRegisteredUser(@Param("user") User user);
}
