package com.quizling.quizservice.mapper;

import com.quizling.quizservice.dto.QuestionDto;
import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.entity.Question;
import com.quizling.quizservice.entity.Quiz;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Convert Quiz dtos to entities and vice versa
 */
public class QuizMapper implements Mapper<QuizDto, Quiz> {
    private final Mapper<QuestionDto, Question> questionMapper;

    public QuizMapper(Mapper<QuestionDto, Question> questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public QuizDto entityToDto(Quiz entity) {
        return QuizDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(entity.getOwner())
                .questions(questionMapper.entityToDto(entity.getQuestions()))
                .created(ZonedDateTime.from(entity.getCreated()))
                .lastUpdated(ZonedDateTime.from(entity.getLastUpdated()))
                .build();
    }

    @Override
    public Quiz dtoToEntity(QuizDto dto) {
        // NOTE: If not populated then dto coming in to be created so we set create time to now (since exact db save time isn't important)
        return Quiz.builder()
                .id(dto.getId())
                .name(dto.getName())
                .owner(dto.getOwner())
                .questions(questionMapper.dtoToEntity(dto.getQuestions()))
                .created(dto.getCreated() != null ? dto.getCreated().toInstant() : Instant.now())
                .lastUpdated(dto.getLastUpdated() != null ? dto.getLastUpdated().toInstant() : Instant.now())
                .build();
    }
}
