package com.quizling.quizservice.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Question {
    private String questionText;
    private List<Answer> answers;
}
