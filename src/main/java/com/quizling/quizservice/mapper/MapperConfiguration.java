package com.quizling.quizservice.mapper;

import com.quizling.quizservice.dto.AnswerDto;
import com.quizling.quizservice.dto.QuestionDto;
import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.entity.Answer;
import com.quizling.quizservice.entity.Question;
import com.quizling.quizservice.entity.Quiz;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * configuration for mappers
 */
@Configuration
public class MapperConfiguration {

    /**
     * Create and register quiz mapper
     * @return quiz mapper
     */
    @Bean
    public Mapper<QuizDto, Quiz> quizMapper() {
        return new QuizMapper(questionMapper());
    }

    // NOTE: The two below aren't beans since currently only used in the quizmapper so wouldn't need to be injected anywhere
    // could change that though if needed/if it seems better

    /**
     * Create answer mapper
     * @return a new answer mapper
     */
    private Mapper<AnswerDto, Answer> answerMapper() {
        return new AnswerMapper();
    }

    /**
     * Create question mapper
     * @return question mapper
     */
    private Mapper<QuestionDto, Question> questionMapper() {
        return new QuestionMapper(answerMapper());
    }
}
