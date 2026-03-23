package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;

public class QuestionResponseDto {
    private Long id;
    private String content;
    private Boolean approved;
    private Boolean anonymous;
    private Boolean answered;
    private Long sessionId;
    private Long askedByUserId;
    private String askedByName;
    private LocalDateTime createdAt;
}