package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Session;

import java.util.List;

public class SessionMapper {

    /** Lightweight list/summary DTO — no qrKey exposed. */
    public static SessionResponseDto toDto(Session session) {
        SessionResponseDto dto = new SessionResponseDto();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());           // session title — NOT event title
        dto.setDescription(session.getDescription());
        dto.setSpeaker(session.getSpeaker());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setAttendanceEnabled(session.getAttendanceEnabled());
        dto.setActive(session.getActive());

        if (session.getEvent() != null) {
            dto.setEventId(session.getEvent().getId());
            // NOTE: do NOT overwrite title here — session and event titles are separate
        }

        dto.setQuestionCount(session.getQuestions() != null ? session.getQuestions().size() : 0);
        dto.setResourceCount(session.getResources() != null ? session.getResources().size() : 0);
        dto.setAttendanceCount(session.getAttendances() != null ? session.getAttendances().size() : 0);
        return dto;
    }

    /** Full detail DTO for Admin/Registrar — includes qrKey. */
    public static SessionDetailDto toDetailDto(Session session) {
        SessionDetailDto dto = new SessionDetailDto();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setSpeaker(session.getSpeaker());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setAttendanceEnabled(session.getAttendanceEnabled());
        dto.setIsActive(session.getActive());
        dto.setQrKey(session.getQrKey());

        if (session.getEvent() != null) {
            dto.setEventId(session.getEvent().getId());
            dto.setEventTitle(session.getEvent().getTitle());
        }

        dto.setAttendanceCount(session.getAttendances() != null ? session.getAttendances().size() : 0);

        if (session.getQuestions() != null) {
            dto.setQuestions(session.getQuestions().stream()
                    .map(QuestionMapper::toDto)
                    .toList());
        }

        if (session.getResources() != null) {
            dto.setResources(session.getResources().stream()
                    .map(ResourceMapper::toDto)
                    .toList());
        }

        return dto;
    }

    public static List<SessionResponseDto> toDtoList(List<Session> sessions) {
        return sessions.stream().map(SessionMapper::toDto).toList();
    }
}
