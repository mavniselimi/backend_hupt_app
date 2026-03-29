package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.DeviceTokenRegisterRequestDto;
import com.hupt.hupt_backend.dto.DeviceTokenResponseDto;
import com.hupt.hupt_backend.entities.DeviceToken;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.DeviceTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/device-tokens")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> registerToken(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeviceTokenRegisterRequestDto request
    ) {
        DeviceToken saved = deviceTokenService.registerToken(userDetails.getId(), request);

        return ResponseEntity.ok(Map.of(
                "message", "Device token registered successfully",
                "id", saved.getId(),
                "platform", saved.getPlatform().name(),
                "token", saved.getToken()
        ));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyTokens(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<DeviceToken> tokens = deviceTokenService.getMyTokens(userDetails.getId());

        List<DeviceTokenResponseDto> response = tokens.stream()
                .map(token -> {
                    DeviceTokenResponseDto dto = new DeviceTokenResponseDto();
                    dto.setId(token.getId());
                    dto.setPlatform(token.getPlatform().name());
                    dto.setToken(token.getToken());
                    dto.setCreatedAt(token.getCreatedAt());
                    dto.setUpdatedAt(token.getUpdatedAt());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteMyToken(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String token
    ) {
        deviceTokenService.deleteMyToken(userDetails.getId(), token);
        return ResponseEntity.ok(Map.of("message", "Device token deleted successfully"));
    }

    @DeleteMapping("/platform/{platform}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteAllMyTokensByPlatform(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String platform
    ) {
        deviceTokenService.deleteAllMyTokensByPlatform(userDetails.getId(), platform);
        return ResponseEntity.ok(Map.of("message", "Platform tokens deleted successfully"));
    }
}