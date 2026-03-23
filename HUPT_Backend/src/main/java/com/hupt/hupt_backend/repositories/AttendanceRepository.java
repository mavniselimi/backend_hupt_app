package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Attendance;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySession(Session session);
    List<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByUser(User user);
    List<Attendance> findByUserId(Long userId);

    boolean existsByUserAndSession(User user, Session session);
    boolean existsByUserIdAndSessionId(Long userId, Long sessionId);

    Optional<Attendance> findByUserIdAndSessionId(Long userId, Long sessionId);

    long countBySessionId(Long sessionId);
    long countByUserId(Long userId);
}