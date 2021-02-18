package com.quizling.quizservice.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Builder
@Data
public class QuizDto extends RepresentationModel<QuizDto> {
    private String id;
    private String name;
    private String owner;
    private List<QuestionDto> questions;
    private Instant created; // NOTE: We just return an instant and let the consumer sort out what timezone to display in
    private Instant lastUpdated;
}
