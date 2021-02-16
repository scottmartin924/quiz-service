package com.quizling.quizservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Data
public class QuestionDto {
    private String questionText;
    private List<AnswerDto> answers;
}
