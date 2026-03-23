package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.QuestionAskRequestDto;
import com.hupt.hupt_backend.entities.Question;
import com.hupt.hupt_backend.security.CustomUserDetails;
import com.hupt.hupt_backend.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity<Question> askQuestion(
            @PathVariable Long sessionId,
            @RequestBody QuestionAskRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Question question = questionService.askQuestion(
                userDetails.getId(),
                sessionId,
                request.getContent(),
                request.getAnonymous()
        );
        return ResponseEntity.ok(question);
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Question>> getQuestionsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(questionService.getQuestionsBySession(sessionId));
    }

    @GetMapping("/session/{sessionId}/approved")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Question>> getApprovedQuestionsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(questionService.getApprovedQuestionsBySession(sessionId));
    }

    @GetMapping("/session/{sessionId}/unanswered")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<Question>> getUnansweredQuestionsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(questionService.getUnansweredQuestionsBySession(sessionId));
    }

    @PatchMapping("/{questionId}/approve")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Question> approveQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.approveQuestion(questionId));
    }
}