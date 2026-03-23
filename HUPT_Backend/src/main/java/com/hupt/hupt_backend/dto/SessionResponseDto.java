package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class SessionResponseDto {
    private Long id;
    private String title;
    private String description;
    private String speaker;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long eventId;
    private Boolean attendanceEnabled;
    private Boolean isActive;
    private int questionCount;
    private int resourceCount;
    private int attendanceCount;
}