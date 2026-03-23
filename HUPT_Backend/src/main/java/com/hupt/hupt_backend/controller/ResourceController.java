package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.ResourceCreateRequestDto;
import com.hupt.hupt_backend.dto.ResourceResponseDto;
import com.hupt.hupt_backend.entities.Resource;
import com.hupt.hupt_backend.dto.ResourceMapper;
import com.hupt.hupt_backend.services.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/session/{sessionId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResourceResponseDto> addResourceToSession(
            @PathVariable Long sessionId,
            @RequestBody ResourceCreateRequestDto request
    ) {
        Resource resource = new Resource();
        resource.setFileName(request.getFileName());
        resource.setFileUrl(request.getFileUrl());
        resource.setType(request.getType());
        resource.setCreatedAt(request.getCreatedAt());

        return ResponseEntity.ok(
                ResourceMapper.toDto(resourceService.addResourceToSession(sessionId, resource))
        );
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ResourceResponseDto>> getResourcesBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                ResourceMapper.toDtoList(resourceService.getResourcesBySession(sessionId))
        );
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ResourceResponseDto>> getResourcesByType(@PathVariable String type) {
        return ResponseEntity.ok(
                ResourceMapper.toDtoList(resourceService.getResourcesByType(type))
        );
    }
}