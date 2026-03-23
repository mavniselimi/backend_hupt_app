package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.AttendanceSubmitRequestDto;
import com.hupt.hupt_backend.entities.Attendance;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<Attendance> submitAttendance(
            @RequestBody AttendanceSubmitRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Attendance attendance = attendanceService.markAttendanceWithQr(
                userDetails.getId(),
                request.getSessionId(),
                request.getQrKey()
        );
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Attendance>> getAttendanceBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendancesBySession(sessionId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<List<Attendance>> getMyAttendances(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUser(userDetails.getId()));
    }

    @GetMapping("/session/{sessionId}/count")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Map<String, Long>> countAttendanceBySession(@PathVariable Long sessionId) {
        long count = attendanceService.countAttendanceBySession(sessionId);
        return ResponseEntity.ok(Map.of("sessionId", sessionId, "count", count));
    }

    @GetMapping("/me/count")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<Map<String, Long>> countMyAttendance(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        long count = attendanceService.countAttendanceByUser(userDetails.getId());
        return ResponseEntity.ok(Map.of("userId", userDetails.getId(), "count", count));
    }
}