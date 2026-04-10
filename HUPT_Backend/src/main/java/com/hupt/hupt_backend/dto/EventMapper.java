package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Event;

import java.util.List;

public class EventMapper {
    public static EventResponseDto toDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setLocation(event.getLocation());
        dto.setPictureOfEventUrl(event.getPictureOfEventUrl());
        dto.setCreatedAt(event.getCreatedAt());

        if (event.getCreatedBy() != null) {
            dto.setCreatedByUserId(event.getCreatedBy().getId());
            dto.setCreatedByName(event.getCreatedBy().getName());
        }

        dto.setSessionCount(event.getSessions() != null ? event.getSessions().size() : 0);
        dto.setRegisteredUserCount(event.getRegistrations() != null ? event.getRegistrations().size() : 0);
        return dto;
    }

    public static List<EventResponseDto> toDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toDto).toList();
    }
}