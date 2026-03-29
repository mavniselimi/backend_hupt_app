package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Session;

import java.util.List;

public class SessionMapper {
    public static SessionResponseDto toDto(Session session) {
        SessionResponseDto dto = new SessionResponseDto();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setSpeaker(session.getSpeaker());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setAttendanceEnabled(session.getAttendanceEnabled());
        dto.setActive(session.getActive());

        if (session.getEvent() != null) {
            dto.setEventId(session.getEvent().getId());
            dto.setTitle(session.getEvent().getTitle());
        }

        dto.setQuestionCount(session.getQuestions() != null ? session.getQuestions().size() : 0);
        dto.setResourceCount(session.getResources() != null ? session.getResources().size() : 0);
        dto.setAttendanceCount(session.getAttendances() != null ? session.getAttendances().size() : 0);
        return dto;
    }

    public static List<SessionResponseDto> toDtoList(List<Session> sessions) {
        return sessions.stream().map(SessionMapper::toDto).toList();
    }
}