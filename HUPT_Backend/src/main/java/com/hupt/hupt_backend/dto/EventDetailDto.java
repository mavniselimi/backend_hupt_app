package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventDetailDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String pictureOfEventUrl;
    private UserSummaryDto createdBy;
    private List<SessionResponseDto> sessions;
    private List<UserSummaryDto> registeredUsers;
    private LocalDateTime createdAt;
}