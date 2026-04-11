package com.hupt.hupt_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Full session detail DTO — returned to Admin/Registrar only.
 * Includes qrKey and aggregated counts.
 */
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
    private String qrKey;
    private List<QuestionResponseDto> questions;
    private List<ResourceResponseDto> resources;
    private int attendanceCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public Boolean getAttendanceEnabled() { return attendanceEnabled; }
    public void setAttendanceEnabled(Boolean attendanceEnabled) { this.attendanceEnabled = attendanceEnabled; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getQrKey() { return qrKey; }
    public void setQrKey(String qrKey) { this.qrKey = qrKey; }

    public List<QuestionResponseDto> getQuestions() { return questions; }
    public void setQuestions(List<QuestionResponseDto> questions) { this.questions = questions; }

    public List<ResourceResponseDto> getResources() { return resources; }
    public void setResources(List<ResourceResponseDto> resources) { this.resources = resources; }

    public int getAttendanceCount() { return attendanceCount; }
    public void setAttendanceCount(int attendanceCount) { this.attendanceCount = attendanceCount; }
}
