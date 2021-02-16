package com.quizling.quizservice.mapper;

import com.quizling.quizservice.dto.AnswerDto;
import com.quizling.quizservice.entity.Answer;

/**
 * Mapper for answer entities and answer dtos
 */
public class AnswerMapper implements Mapper<AnswerDto, Answer> {
    @Override
    public AnswerDto entityToDto(Answer entity) {
        return AnswerDto.builder()
                .answerText(entity.getAnswerText())
                .isCorrect(entity.isCorrect())
                .build();
    }

    @Override
    public Answer dtoToEntity(AnswerDto dto) {
        return Answer.builder()
                .answerText(dto.getAnswerText())
                .isCorrect(dto.isCorrect())
                .build();
    }
}
