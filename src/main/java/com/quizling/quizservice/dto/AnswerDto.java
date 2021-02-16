package com.quizling.quizservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnswerDto {
    private String answerText;
    private boolean isCorrect;
}
