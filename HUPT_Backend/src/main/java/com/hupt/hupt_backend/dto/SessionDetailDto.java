package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SessionDetailDto {
    private Long id;
    private String title;
    private String description;
    private String speaker;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long eventId;
    private String eventTitle;
    private Boolean attendanceEnabled;
    private Boolean isActive;
    private String qrKey; // sadece organizer view için dikkatli
    private List<QuestionResponseDto> questions;
    private List<ResourceResponseDto> resources;
    private int attendanceCount;
}