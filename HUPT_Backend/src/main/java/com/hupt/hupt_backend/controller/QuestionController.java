package com.hupt.hupt_backend.controller;

import com.hupt.hupt_backend.dto.QuestionAskRequestDto;
import com.hupt.hupt_backend.dto.QuestionResponseDto;
import com.hupt.hupt_backend.entities.Question;
import com.hupt.hupt_backend.dto.QuestionMapper;
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
    public ResponseEntity<QuestionResponseDto> askQuestion(
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
        return ResponseEntity.ok(QuestionMapper.toDto(question));
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                QuestionMapper.toDtoList(questionService.getQuestionsBySession(sessionId))
        );
    }

    @GetMapping("/session/{sessionId}/approved")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<QuestionResponseDto>> getApprovedQuestionsBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(
                QuestionMapper.toDtoList(questionService.getApprovedQuestionsBySession(sessionId))
        );
    }

    @PatchMapping("/{questionId}/approve")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<QuestionResponseDto> approveQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(
                QuestionMapper.toDto(questionService.approveQuestion(questionId))
        );
    }
}