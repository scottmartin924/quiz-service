package com.quizling.quizservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Data
public class QuizDto {
    private String id;
    private String name;
    private String owner;
    private List<QuestionDto> questions;
    private ZonedDateTime created;
    private ZonedDateTime lastUpdated;
}
