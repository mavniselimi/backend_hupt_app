package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.EventRegistration;

import java.util.List;

public class EventRegistrationMapper {

    public static EventRegistrationResponseDto toDto(EventRegistration reg) {
        EventRegistrationResponseDto dto = new EventRegistrationResponseDto();
        dto.setId(reg.getId());
        dto.setQueueNumber(reg.getQueueNumber());
        dto.setStatus(reg.getStatus() != null ? reg.getStatus().name() : null);
        dto.setRegisteredAt(reg.getRegisteredAt());
        dto.setCardIssuedAt(reg.getCardIssuedAt());

        if (reg.getEvent() != null) {
            dto.setEventId(reg.getEvent().getId());
            dto.setEventTitle(reg.getEvent().getTitle());
        }

        if (reg.getUser() != null) {
            dto.setUserId(reg.getUser().getId());
            dto.setUserName(reg.getUser().getName());
            dto.setUserEmail(reg.getUser().getEmail());
        }

        if (reg.getProcessedBy() != null) {
            dto.setProcessedById(reg.getProcessedBy().getId());
            dto.setProcessedByName(reg.getProcessedBy().getName());
        }

        return dto;
    }

    public static List<EventRegistrationResponseDto> toDtoList(List<EventRegistration> registrations) {
        return registrations.stream().map(EventRegistrationMapper::toDto).toList();
    }
}
