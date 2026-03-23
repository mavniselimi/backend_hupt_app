package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.repositories.EventRepository;
import com.hupt.hupt_backend.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
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
        return sessionRepository.save(session);
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