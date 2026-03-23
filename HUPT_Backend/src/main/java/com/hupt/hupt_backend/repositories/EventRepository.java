package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreatedBy(User user);
    List<Event> findByRegisteredUsersContaining(User user);
}