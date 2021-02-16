package com.quizling.quizservice.mapper;

import com.quizling.quizservice.dto.AnswerDto;
import com.quizling.quizservice.dto.QuestionDto;
import com.quizling.quizservice.entity.Answer;
import com.quizling.quizservice.entity.Question;

import java.util.stream.Collectors;

/**
 * Mapper for question dto and question entity
 */
public class QuestionMapper implements Mapper<QuestionDto, Question> {
    private final Mapper<AnswerDto, Answer> answerMapper;

    public QuestionMapper(Mapper<AnswerDto, Answer> answerMapper) {
        this.answerMapper = answerMapper;
    }

    @Override
    public QuestionDto entityToDto(Question entity) {
        return QuestionDto.builder()
                .questionText(entity.getQuestionText())
                .answers(answerMapper.entityToDto(entity.getAnswers()))
                .build();
    }

    @Override
    public Question dtoToEntity(QuestionDto dto) {
        return Question.builder()
                .questionText(dto.getQuestionText())
                .answers(answerMapper.dtoToEntity(dto.getAnswers()))
                .build();
    }
}
