package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.Attendance;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.repositories.AttendanceRepository;
import com.hupt.hupt_backend.repositories.SessionRepository;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            SessionRepository sessionRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public Attendance markAttendanceWithQr(Long userId, Long sessionId, String qrKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (Boolean.FALSE.equals(session.getActive())) {
            throw new RuntimeException("Session is not active");
        }

        if (Boolean.FALSE.equals(session.getAttendanceEnabled())) {
            throw new RuntimeException("Attendance is not enabled for this session");
        }

        if (!session.getQrKey().equals(qrKey)) {
            throw new RuntimeException("Invalid QR key");
        }

        boolean alreadyExists = attendanceRepository.existsByUserAndSession(user, session);
        if (alreadyExists) {
            throw new RuntimeException("Attendance already submitted");
        }

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setSession(session);
        attendance.setAttendedAt(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendancesBySession(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId);
    }

    public List<Attendance> getAttendancesByUser(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    public long countAttendanceBySession(Long sessionId) {
        return attendanceRepository.countBySessionId(sessionId);
    }

    public long countAttendanceByUser(Long userId) {
        return attendanceRepository.countByUserId(userId);
    }
}