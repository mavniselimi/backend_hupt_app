package com.hupt.hupt_backend.auth;

import com.hupt.hupt_backend.auth.dto.AuthLoginRequest;
import com.hupt.hupt_backend.auth.dto.AuthResponse;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.entities.UserType;
import com.hupt.hupt_backend.dto.UserMapper;
import com.hupt.hupt_backend.repositories.UserRepository;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthResponse login(AuthLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(new CustomUserDetails(user));

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(UserMapper.toSummaryDto(user));

        return response;
    }
}