package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.UserSummaryDto;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.dto.UserMapper;
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
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<UserSummaryDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.getUserById(userDetails.getId()))
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers().stream().map(UserMapper::toSummaryDto).toList()
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(
                UserMapper.toSummaryDto(userService.getUserById(userId))
        );
    }
}