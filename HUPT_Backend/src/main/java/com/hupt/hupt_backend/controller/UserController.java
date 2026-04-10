package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.UserMapper;
import com.hupt.hupt_backend.dto.UserSummaryDto;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User','Registrar')")
    public ResponseEntity<UserSummaryDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.getUserById(userDetails.getId())));
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers().stream().map(UserMapper::toSummaryDto).toList());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.getUserById(userId)));
    }

    /**
     * Get all users with the Registrar role — used by admin to pick desks for an event.
     * GET /api/users/registrars
     */
    @GetMapping("/registrars")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserSummaryDto>> getAllRegistrars() {
        return ResponseEntity.ok(
                userService.getAllRegistrars().stream().map(UserMapper::toSummaryDto).toList());
    }

    /**
     * Activate a registrar desk — they will start receiving new registrations.
     * PATCH /api/users/{userId}/active
     */
    @PatchMapping("/{userId}/active")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserSummaryDto> activateRegistrar(@PathVariable Long userId) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.setActive(userId, true)));
    }

    /**
     * Deactivate a registrar desk — they will stop receiving new registrations.
     * Existing queue entries are NOT affected.
     * PATCH /api/users/{userId}/inactive
     */
    @PatchMapping("/{userId}/inactive")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserSummaryDto> deactivateRegistrar(@PathVariable Long userId) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.setActive(userId, false)));
    }
}
