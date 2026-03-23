package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Attendance;

import java.util.List;

public class AttendanceMapper {
    public static AttendanceResponseDto toDto(Attendance attendance) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(attendance.getId());
        dto.setAttendedAt(attendance.getAttendedAt());

        if (attendance.getUser() != null) {
            dto.setUserId(attendance.getUser().getId());
            dto.setUserName(attendance.getUser().getName());
        }

        if (attendance.getSession() != null) {
            dto.setSessionId(attendance.getSession().getId());
            dto.setSessionTitle(attendance.getSession().getTitle());
        }

        return dto;
    }

    public static List<AttendanceResponseDto> toDtoList(List<Attendance> attendances) {
        return attendances.stream().map(AttendanceMapper::toDto).toList();
    }
}