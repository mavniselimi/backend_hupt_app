package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.SessionCreateRequestDto;
import com.hupt.hupt_backend.dto.SessionResponseDto;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.dto.SessionMapper;
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
    public ResponseEntity<SessionResponseDto> createSession(
            @PathVariable Long eventId,
            @RequestBody SessionCreateRequestDto request
    ) {
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setSpeaker(request.getSpeaker());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());

        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.createSession(eventId, session))
        );
    }

    @GetMapping("/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SessionResponseDto> getSessionById(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.getSessionById(sessionId))
        );
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SessionResponseDto>> getSessionsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                SessionMapper.toDtoList(sessionService.getSessionsByEventId(eventId))
        );
    }

    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SessionResponseDto>> getActiveSessions() {
        return ResponseEntity.ok(
                SessionMapper.toDtoList(sessionService.getActiveSessions())
        );
    }

    @PatchMapping("/{sessionId}/activate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<SessionResponseDto> activateSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.activateSession(sessionId))
        );
    }

    @PatchMapping("/{sessionId}/deactivate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<SessionResponseDto> deactivateSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.deactivateSession(sessionId))
        );
    }

    @PatchMapping("/{sessionId}/attendance/enable")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<SessionResponseDto> enableAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.enableAttendance(sessionId))
        );
    }

    @PatchMapping("/{sessionId}/attendance/disable")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<SessionResponseDto> disableAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.disableAttendance(sessionId))
        );
    }

    @PatchMapping("/{sessionId}/qr/regenerate")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<SessionResponseDto> regenerateQrKey(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                SessionMapper.toDto(sessionService.regenerateQrKey(sessionId))
        );
    }
}