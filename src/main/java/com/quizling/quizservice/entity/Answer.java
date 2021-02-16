package com.quizling.quizservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Answer {
    private String answerText;
    private boolean isCorrect;
}
