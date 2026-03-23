package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.EventCreateRequestDto;
import com.hupt.hupt_backend.dto.EventResponseDto;
import com.hupt.hupt_backend.entities.Event;
import com.hupt.hupt_backend.dto.EventMapper;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.EventService;
import com.hupt.hupt_backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(
                EventMapper.toDtoList(eventService.getAllEvents())
        );
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(
                EventMapper.toDto(eventService.getEventById(eventId))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventResponseDto> createEvent(
            @RequestBody EventCreateRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        event.setPictureOfEventUrl(request.getPictureOfEventUrl());

        Event created = eventService.createEvent(event, userDetails.getId());
        return ResponseEntity.ok(EventMapper.toDto(created));
    }

    @PostMapping("/{eventId}/register/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventResponseDto> registerUserToEvent(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                EventMapper.toDto(eventService.registerUserToEvent(eventId, userId))
        );
    }

    @DeleteMapping("/{eventId}/register/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<EventResponseDto> removeUserFromEvent(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                EventMapper.toDto(eventService.removeUserFromEvent(eventId, userId))
        );
    }

    @PostMapping("/{eventId}/register/me")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<EventResponseDto> registerMeToEvent(
            @PathVariable Long eventId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                EventMapper.toDto(eventService.registerUserToEvent(eventId, userDetails.getId()))
        );
    }

    @GetMapping("/me/registered")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<List<EventResponseDto>> getMyRegisteredEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                EventMapper.toDtoList(eventService.getRegisteredEventsOfUser(userDetails.getId()))
        );
    }

    @GetMapping("/me/created")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<EventResponseDto>> getMyCreatedEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                EventMapper.toDtoList(eventService.getEventsCreatedByUser(userDetails.getId()))
        );
    }
}