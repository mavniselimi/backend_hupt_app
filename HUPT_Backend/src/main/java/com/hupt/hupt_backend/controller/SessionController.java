package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.SessionCreateRequestDto;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.services.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/event/{eventId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> createSession(
            @PathVariable Long eventId,
            @RequestBody SessionCreateRequestDto request
    ) {
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSpeaker(request.getSpeaker());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());

        return ResponseEntity.ok(sessionService.createSession(eventId, session));
    }

    @GetMapping("/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Session> getSessionById(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.getSessionById(sessionId));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Session>> getSessionsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(sessionService.getSessionsByEventId(eventId));
    }

    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Session>> getActiveSessions() {
        return ResponseEntity.ok(sessionService.getActiveSessions());
    }

    @PatchMapping("/{sessionId}/activate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> activateSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.activateSession(sessionId));
    }

    @PatchMapping("/{sessionId}/deactivate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> deactivateSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.deactivateSession(sessionId));
    }

    @PatchMapping("/{sessionId}/attendance/enable")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> enableAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.enableAttendance(sessionId));
    }

    @PatchMapping("/{sessionId}/attendance/disable")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> disableAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.disableAttendance(sessionId));
    }

    @PatchMapping("/{sessionId}/qr/regenerate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Session> regenerateQrKey(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.regenerateQrKey(sessionId));
    }
}