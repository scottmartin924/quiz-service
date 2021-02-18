package com.quizling.quizservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// NOTE: Idea for enhancement: allow public and private quizzes (would need user authentication/authorization as well)

@Document("quizzes")
@Data
@NoArgsConstructor
public class Quiz extends BaseEntity {
    @Id
    private String id;
    private String name;
    private String owner;
    private List<Question> questions;

    @Builder
    public Quiz(String id,
                String name,
                List<Question> questions,
                String owner,
                Instant created,
                Instant lastUpdated) {
        this.id = id;
        this.name = name;
        this.questions = questions;
        this.created = created;
        this.lastUpdated = lastUpdated;
        this.owner = owner;
    }
}
