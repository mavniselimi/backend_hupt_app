package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.repositories.EventRepository;
import com.hupt.hupt_backend.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository, NotificationService notificationService) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    public Session createSession(Long eventId, Session session) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        session.setEvent(event);

        if (session.getActive() == null) {
            session.setActive(false);
        }

        if (session.getAttendanceEnabled() == null) {
            session.setAttendanceEnabled(false);
        }

        if (session.getQrKey() == null || session.getQrKey().isBlank()) {
            session.setQrKey(UUID.randomUUID().toString());
        }

        return sessionRepository.save(session);
    }

    public Session getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
    }

    public List<Session> getSessionsByEventId(Long eventId) {
        return sessionRepository.findByEventId(eventId);
    }

    public List<Session> getActiveSessions() {
        return sessionRepository.findByIsActiveTrue();
    }

    public Session activateSession(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setActive(true);
        Session saved = sessionRepository.save(session);

        Event event = saved.getEvent();
        if (event != null && event.getRegisteredUsers() != null) {
            for (User user : event.getRegisteredUsers()) {
                try {
                    notificationService.sendSessionStartedToUser(
                            user.getId(),
                            event.getId(),
                            saved.getId(),
                            saved.getTitle()
                    );
                } catch (Exception e) {
                    System.out.println("Notification failed for user " + user.getId() + ": " + e.getMessage());
                }
            }
        }

        return saved;
    }


    public Session deactivateSession(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setActive(false);
        return sessionRepository.save(session);
    }

    public Session enableAttendance(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setAttendanceEnabled(true);
        return sessionRepository.save(session);
    }

    public Session disableAttendance(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setAttendanceEnabled(false);
        return sessionRepository.save(session);
    }

    public Session regenerateQrKey(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setQrKey(UUID.randomUUID().toString());
        return sessionRepository.save(session);
    }

    public Session getSessionByQrKey(String qrKey) {
        return sessionRepository.findByQrKey(qrKey)
                .orElseThrow(() -> new RuntimeException("Session not found for qrKey"));
    }
}