package com.quizling.quizservice.mapper;

import com.quizling.quizservice.dto.QuestionDto;
import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.entity.Question;
import com.quizling.quizservice.entity.Quiz;
import com.quizling.quizservice.web.QuizController;
import org.springframework.hateoas.Link;

import java.time.Instant;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        final QuizDto dto = QuizDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .owner(entity.getOwner())
                .questions(questionMapper.entityToDto(entity.getQuestions()))
                .created(entity.getCreated())
                .lastUpdated(entity.getLastUpdated())
                .build();
        dto.add(createSelfLink(dto));
        return dto;
    }

    @Override
    public Quiz dtoToEntity(QuizDto dto) {
        // NOTE: If not populated then dto coming in to be created so we set create time to now (since exact db save time isn't important)
        return Quiz.builder()
                .id(dto.getId())
                .name(dto.getName())
                .owner(dto.getOwner())
                .questions(questionMapper.dtoToEntity(dto.getQuestions()))
                .created(dto.getCreated() != null ? dto.getCreated() : Instant.now())
                .lastUpdated(dto.getLastUpdated() != null ? dto.getLastUpdated() : Instant.now())
                .build();
    }

    /**
     * Create self link to add to dto
     * @param quiz the quiz dto to add the link to
     * @return the self link
     */
    private Link createSelfLink(final QuizDto quiz) {
        return linkTo(methodOn(QuizController.class).findQuiz(quiz.getId(), quiz.getOwner())).withSelfRel();
    }
}
