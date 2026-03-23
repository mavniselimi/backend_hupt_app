package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Resource;

import java.util.List;

public class ResourceMapper {
    public static ResourceResponseDto toDto(Resource resource) {
        ResourceResponseDto dto = new ResourceResponseDto();
        dto.setId(resource.getId());
        dto.setFileName(resource.getFileName());
        dto.setFileUrl(resource.getFileUrl());
        dto.setType(resource.getType());
        dto.setCreatedAt(resource.getCreatedAt());
        dto.setLoadedAt(resource.getLoadedAt());

        if (resource.getSession() != null) {
            dto.setSessionId(resource.getSession().getId());
        }

        return dto;
    }

    public static List<ResourceResponseDto> toDtoList(List<Resource> resources) {
        return resources.stream().map(ResourceMapper::toDto).toList();
    }
}