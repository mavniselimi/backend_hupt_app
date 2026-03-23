package com.hupt.hupt_backend.services;

import com.hupt.hupt_backend.entities.Question;
import com.hupt.hupt_backend.entities.Session;
import com.hupt.hupt_backend.entities.User;
import com.hupt.hupt_backend.repositories.QuestionRepository;
import com.hupt.hupt_backend.repositories.SessionRepository;
import com.hupt.hupt_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public QuestionService(
            QuestionRepository questionRepository,
            SessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.questionRepository = questionRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public Question askQuestion(Long userId, Long sessionId, String content, Boolean anonymous) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (Boolean.FALSE.equals(session.getActive())) {
            throw new RuntimeException("Cannot ask question. Session is not active");
        }

        Question question = new Question();
        question.setAskedBy(user);
        question.setSession(session);
        question.setContent(content);
        question.setAnonymous(anonymous != null ? anonymous : false);
        question.setApproved(false);
        question.setAnswered(false);
        question.setCreatedAt(LocalDateTime.now());

        return questionRepository.save(question);
    }

    public List<Question> getQuestionsBySession(Long sessionId) {
        return questionRepository.findBySessionId(sessionId);
    }

    public List<Question> getApprovedQuestionsBySession(Long sessionId) {
        return questionRepository.findBySessionIdAndApprovedTrue(sessionId);
    }

    public List<Question> getUnansweredQuestionsBySession(Long sessionId) {
        return questionRepository.findBySessionIdAndAnsweredFalse(sessionId);
    }

    public Question approveQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setApproved(true);
        return questionRepository.save(question);
    }

    public Question answerQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setAnswered(true);

        return questionRepository.save(question);
    }
}