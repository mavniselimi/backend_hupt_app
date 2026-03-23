package com.hupt.hupt_backend.dto;

import com.hupt.hupt_backend.entities.Question;

import java.util.List;

public class QuestionMapper {
    public static QuestionResponseDto toDto(Question question) {
        QuestionResponseDto dto = new QuestionResponseDto();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setApproved(question.getApproved());
        dto.setAnonymous(question.getAnonymous());
        dto.setCreatedAt(question.getCreatedAt());

        if (question.getSession() != null) {
            dto.setSessionId(question.getSession().getId());
        }

        if (question.getAskedBy() != null) {
            dto.setAskedByUserId(question.getAskedBy().getId());
            dto.setAskedByName(Boolean.TRUE.equals(question.getAnonymous()) ? null : question.getAskedBy().getName());
        }

        return dto;
    }

    public static List<QuestionResponseDto> toDtoList(List<Question> questions) {
        return questions.stream().map(QuestionMapper::toDto).toList();
    }
}