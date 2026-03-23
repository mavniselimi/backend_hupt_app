package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class AttendanceResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long sessionId;
    private String sessionTitle;
    private LocalDateTime attendedAt;
}