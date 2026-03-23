package com.hupt.hupt_backend.repositories;

import com.hupt.hupt_backend.entities.Question;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySession(Session session);
    List<Question> findBySessionId(Long sessionId);
    List<Question> findBySessionIdAndApprovedTrue(Long sessionId);
    List<Question> findByAskedBy(User user);
    List<Question> findBySessionIdAndAnsweredFalse(Long sessionId);
}