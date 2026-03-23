package com.hupt.hupt_backend.dto;


public class UserAttendanceCountResponseDto {
    private Long userId;
    private Long count;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}