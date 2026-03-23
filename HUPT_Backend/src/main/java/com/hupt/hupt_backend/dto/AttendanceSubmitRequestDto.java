package com.hupt.hupt_backend.dto;

public class AttendanceSubmitRequestDto {
    private Long sessionId;
    private String qrKey;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getQrKey() {
        return qrKey;
    }

    public void setQrKey(String qrKey) {
        this.qrKey = qrKey;
    }
}