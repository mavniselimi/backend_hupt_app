package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.AttendanceSubmitRequestDto;
import com.hupt.hupt_backend.dto.AttendanceResponseDto;
import com.hupt.hupt_backend.dto.SessionAttendanceCountResponseDto;
import com.hupt.hupt_backend.dto.UserAttendanceCountResponseDto;
import com.hupt.hupt_backend.entities.Attendance;
import com.hupt.hupt_backend.dto.AttendanceMapper;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('Admin','User','Registrar')")
    public ResponseEntity<AttendanceResponseDto> submitAttendance(
            @RequestBody AttendanceSubmitRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Attendance attendance = attendanceService.markAttendanceWithQr(
                userDetails.getId(),
                request.getSessionId(),
                request.getQrKey()
        );
        return ResponseEntity.ok(AttendanceMapper.toDto(attendance));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<List<AttendanceResponseDto>> getAttendanceBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                AttendanceMapper.toDtoList(attendanceService.getAttendancesBySession(sessionId))
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<List<AttendanceResponseDto>> getMyAttendances(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                AttendanceMapper.toDtoList(attendanceService.getAttendancesByUser(userDetails.getId()))
        );
    }

    @GetMapping("/session/{sessionId}/count")
    @PreAuthorize("hasAnyRole('Admin','Registrar')")
    public ResponseEntity<SessionAttendanceCountResponseDto> countAttendanceBySession(@PathVariable Long sessionId) {
        SessionAttendanceCountResponseDto dto = new SessionAttendanceCountResponseDto();
        dto.setSessionId(sessionId);
        dto.setCount(attendanceService.countAttendanceBySession(sessionId));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me/count")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<UserAttendanceCountResponseDto> countMyAttendance(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserAttendanceCountResponseDto dto = new UserAttendanceCountResponseDto();
        dto.setUserId(userDetails.getId());
        dto.setCount(attendanceService.countAttendanceByUser(userDetails.getId()));
        return ResponseEntity.ok(dto);
    }
}