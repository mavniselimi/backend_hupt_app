package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.ResourceCreateRequestDto;
import com.hupt.hupt_backend.entities.Resource;
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
    public ResponseEntity<Resource> addResourceToSession(
            @PathVariable Long sessionId,
            @RequestBody ResourceCreateRequestDto request
    ) {
        Resource resource = new Resource();
        resource.setFileName(request.getFileName());
        resource.setFileUrl(request.getFileUrl());
        resource.setType(request.getType());
        resource.setCreatedAt(request.getCreatedAt());

        return ResponseEntity.ok(resourceService.addResourceToSession(sessionId, resource));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Resource>> getResourcesBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(resourceService.getResourcesBySession(sessionId));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Resource>> getResourcesByType(@PathVariable String type) {
        return ResponseEntity.ok(resourceService.getResourcesByType(type));
    }
}