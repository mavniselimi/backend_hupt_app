package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.User;

public class UserMapper {
    public static UserSummaryDto toSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}