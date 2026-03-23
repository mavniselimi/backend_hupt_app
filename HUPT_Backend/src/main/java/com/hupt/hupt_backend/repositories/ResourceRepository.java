package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Resource;
import com.hupt.hupt_backend.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findBySession(Session session);
    List<Resource> findBySessionId(Long sessionId);
    List<Resource> findByType(String type);
}