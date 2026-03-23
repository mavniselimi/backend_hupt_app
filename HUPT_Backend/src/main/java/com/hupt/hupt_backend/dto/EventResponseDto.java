package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String pictureOfEventUrl;
    private Long createdByUserId;
    private String createdByName;
    private int sessionCount;
    private int registeredUserCount;
    private LocalDateTime createdAt;
}