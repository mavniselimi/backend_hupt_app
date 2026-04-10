package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.EventRegistrationMapper;
import com.hupt.hupt_backend.dto.EventRegistrationResponseDto;
import com.hupt.hupt_backend.entities.EventRegistration;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/registrations")
public class EventRegistrationController {

    private final EventRegistrationService registrationService;

    public EventRegistrationController(EventRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * User registers themselves for an event.
     * POST /api/events/{eventId}/registrations/me
     */
    @PostMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User','Registrar')")
    public ResponseEntity<EventRegistrationResponseDto> registerMe(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        EventRegistration reg = registrationService.registerUser(eventId, userDetails.getId());
        return ResponseEntity.ok(EventRegistrationMapper.toDto(reg));
    }

    /**
     * Admin registers a specific user for an event.
     * POST /api/events/{eventId}/registrations/users/{userId}
     */
    @PostMapping("/users/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventRegistrationResponseDto> registerUser(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        EventRegistration reg = registrationService.registerUser(eventId, userId);
        return ResponseEntity.ok(EventRegistrationMapper.toDto(reg));
    }

    /**
     * User removes their own registration.
     * DELETE /api/events/{eventId}/registrations/me
     */
    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<Void> unregisterMe(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        registrationService.unregisterUser(eventId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Admin removes a specific user's registration.
     * DELETE /api/events/{eventId}/registrations/users/{userId}
     */
    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> unregisterUser(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        registrationService.unregisterUser(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the full queue for an event (all registrations ordered by queue number).
     * Visible to Admin and Registrar.
     * GET /api/events/{eventId}/registrations/queue
     */
    @GetMapping("/queue")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<EventRegistrationResponseDto>> getFullQueue(
            @PathVariable Long eventId
    ) {
        List<EventRegistration> queue = registrationService.getQueueForEvent(eventId);
        return ResponseEntity.ok(EventRegistrationMapper.toDtoList(queue));
    }

    /**
     * Get only the PENDING registrations in order.
     * GET /api/events/{eventId}/registrations/queue/pending
     */
    @GetMapping("/queue/pending")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<EventRegistrationResponseDto>> getPendingQueue(
            @PathVariable Long eventId
    ) {
        List<EventRegistration> queue = registrationService.getPendingQueueForEvent(eventId);
        return ResponseEntity.ok(EventRegistrationMapper.toDtoList(queue));
    }

    /**
     * Get the next PENDING person the registrar should call up.
     * GET /api/events/{eventId}/registrations/queue/next
     */
    @GetMapping("/queue/next")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<EventRegistrationResponseDto> getNextInQueue(
            @PathVariable Long eventId
    ) {
        return registrationService.getNextInQueue(eventId)
                .map(EventRegistrationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Registrar marks a person's card as issued.
     * POST /api/events/{eventId}/registrations/{registrationId}/issue-card
     */
    @PostMapping("/{registrationId}/issue-card")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<EventRegistrationResponseDto> issueCard(
            @PathVariable Long eventId,
            @PathVariable Long registrationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        EventRegistration updated = registrationService.issueCard(eventId, registrationId, userDetails.getId());
        return ResponseEntity.ok(EventRegistrationMapper.toDto(updated));
    }

    /**
     * Get the current user's registration status for an event (shows queue number etc).
     * GET /api/events/{eventId}/registrations/me
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User','Registrar')")
    public ResponseEntity<EventRegistrationResponseDto> getMyRegistration(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return registrationService.getMyRegistration(eventId, userDetails.getId())
                .map(EventRegistrationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
