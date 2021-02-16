package com.quizling.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class QuizListDto {
    private List<QuizDto> quizzes;
}
