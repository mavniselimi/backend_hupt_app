package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.Resource;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.repositories.ResourceRepository;
import com.hupt.hupt_backend.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final SessionRepository sessionRepository;

    public ResourceService(ResourceRepository resourceRepository, SessionRepository sessionRepository) {
        this.resourceRepository = resourceRepository;
        this.sessionRepository = sessionRepository;
    }

    public Resource addResourceToSession(Long sessionId, Resource resource) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        resource.setSession(session);

        if (resource.getLoadedAt() == null) {
            resource.setLoadedAt(LocalDateTime.now());
        }

        return resourceRepository.save(resource);
    }

    public List<Resource> getResourcesBySession(Long sessionId) {
        return resourceRepository.findBySessionId(sessionId);
    }

    public List<Resource> getResourcesByType(String type) {
        return resourceRepository.findByType(type);
    }
}