package com.hupt.hupt_backend.auth;


import com.hupt.hupt_backend.auth.AuthenticationService;
import com.hupt.hupt_backend.auth.dto.AuthLoginRequest;
import com.hupt.hupt_backend.auth.dto.AuthResponse;
import com.hupt.hupt_backend.dto.UserSummaryDto;
import com.hupt.hupt_backend.dto.UserMapper;
import com.hupt.hupt_backend.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummaryDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(UserMapper.toSummaryDto(userDetails.getUserEntity()));
    }
}