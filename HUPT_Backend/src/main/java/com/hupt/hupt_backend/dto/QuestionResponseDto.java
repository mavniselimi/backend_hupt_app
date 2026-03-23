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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getAskedByUserId() {
        return askedByUserId;
    }

    public void setAskedByUserId(Long askedByUserId) {
        this.askedByUserId = askedByUserId;
    }

    public String getAskedByName() {
        return askedByName;
    }

    public void setAskedByName(String askedByName) {
        this.askedByName = askedByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}