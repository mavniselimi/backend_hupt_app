package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class ResourceResponseDto {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String type;
    private Long sessionId;
    private LocalDateTime createdAt;
    private LocalDateTime loadedAt;
}