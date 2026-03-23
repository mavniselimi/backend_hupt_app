package com.hupt.hupt_backend.dto;


public class SessionAttendanceCountResponseDto {
    private Long sessionId;
    private Long count;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}