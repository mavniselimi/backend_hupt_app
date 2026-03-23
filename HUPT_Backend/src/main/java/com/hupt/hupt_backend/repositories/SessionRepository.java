package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByEvent(Event event);
    List<Session> findByEventId(Long eventId);
    List<Session> findByIsActiveTrue();
    List<Session> findByEventIdAndIsActiveTrue(Long eventId);

    Optional<Session> findByQrKey(String qrKey);
}