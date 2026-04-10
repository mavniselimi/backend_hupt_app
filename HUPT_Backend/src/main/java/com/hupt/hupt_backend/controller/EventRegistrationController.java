package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.EventMapper;
import com.hupt.hupt_backend.dto.EventRegistrationMapper;
import com.hupt.hupt_backend.dto.EventRegistrationResponseDto;
import com.hupt.hupt_backend.dto.EventResponseDto;
import com.hupt.hupt_backend.dto.UserMapper;
import com.hupt.hupt_backend.dto.UserSummaryDto;
import com.hupt.hupt_backend.entities.EventRegistration;
import com.hupt.hupt_backend.entities.User;
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

    // ══════════════════════════════════════════════════════════════════════════════
    // USER SELF-REGISTRATION
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * User registers themselves for an event.
     * The backend automatically routes them to the least-loaded active desk.
     * Response tells the frontend which desk and queue number they got.
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
     * User cancels their own registration.
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
     * Get the current user's registration — shows which desk and queue number they got.
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

    // ══════════════════════════════════════════════════════════════════════════════
    // QUEUE VIEWS
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Full event queue across all desks — for admin overview.
     * Ordered by desk, then by queue position within each desk.
     * GET /api/events/{eventId}/registrations/queue
     */
    @GetMapping("/queue")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<EventRegistrationResponseDto>> getFullQueue(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(
                EventRegistrationMapper.toDtoList(
                        registrationService.getFullQueueForEvent(eventId)));
    }

    /**
     * Queue for a specific desk — for the registrar to see their own list.
     * GET /api/events/{eventId}/registrations/queue/desk/{registrarId}
     */
    @GetMapping("/queue/desk/{registrarId}")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<EventRegistrationResponseDto>> getDeskQueue(
            @PathVariable Long eventId,
            @PathVariable Long registrarId
    ) {
        return ResponseEntity.ok(
                EventRegistrationMapper.toDtoList(
                        registrationService.getQueueForRegistrar(eventId, registrarId)));
    }

    /**
     * A registrar sees their own current desk queue (only PENDING).
     * GET /api/events/{eventId}/registrations/queue/my
     */
    @GetMapping("/queue/my")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<EventRegistrationResponseDto>> getMyDeskQueue(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                EventRegistrationMapper.toDtoList(
                        registrationService.getPendingQueueForRegistrar(eventId, userDetails.getId())));
    }

    /**
     * The next person the calling registrar should process.
     * GET /api/events/{eventId}/registrations/queue/my/next
     */
    @GetMapping("/queue/my/next")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<EventRegistrationResponseDto> getNextInMyQueue(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return registrationService.getNextInQueueForRegistrar(eventId, userDetails.getId())
                .map(EventRegistrationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    // ══════════════════════════════════════════════════════════════════════════════
    // CARD ISSUANCE
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Registrar marks a person's card as issued — moves them from PENDING to CARD_ISSUED.
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

    // ══════════════════════════════════════════════════════════════════════════════
    // REGISTRAR ASSIGNMENT (Admin only)
    // ══════════════════════════════════════════════════════════════════════════════

    /**
     * Get all registrars assigned to this event.
     * GET /api/events/{eventId}/registrations/registrars
     */
    @GetMapping("/registrars")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<UserSummaryDto>> getAssignedRegistrars(
            @PathVariable Long eventId
    ) {
        List<User> registrars = registrationService.getRegistrarsForEvent(eventId);
        return ResponseEntity.ok(registrars.stream().map(UserMapper::toSummaryDto).toList());
    }

    /**
     * Assign a registrar (desk) to this event.
     * POST /api/events/{eventId}/registrations/registrars/{registrarId}
     */
    @PostMapping("/registrars/{registrarId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventResponseDto> assignRegistrar(
            @PathVariable Long eventId,
            @PathVariable Long registrarId
    ) {
        return ResponseEntity.ok(
                EventMapper.toDto(
                        registrationService.assignRegistrarToEvent(eventId, registrarId)));
    }

    /**
     * Remove a registrar (desk) from this event.
     * Their existing queue entries are NOT deleted.
     * DELETE /api/events/{eventId}/registrations/registrars/{registrarId}
     */
    @DeleteMapping("/registrars/{registrarId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventResponseDto> removeRegistrar(
            @PathVariable Long eventId,
            @PathVariable Long registrarId
    ) {
        return ResponseEntity.ok(
                EventMapper.toDto(
                        registrationService.removeRegistrarFromEvent(eventId, registrarId)));
    }
}
