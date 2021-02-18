package com.quizling.quizservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.dto.QuizListDto;
import com.quizling.quizservice.entity.Quiz;
import com.quizling.quizservice.error.QuizServiceException;
import com.quizling.quizservice.mapper.Mapper;
import com.quizling.quizservice.mapper.MapperConfiguration;
import com.quizling.quizservice.repository.QuizRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(MapperConfiguration.class)
public class QuizServiceTest {

    @Autowired
    Mapper<QuizDto, Quiz> mapper;

    @Mock
    private QuizRepository quizRepository;
    private QuizService quizService;

    private List<Quiz> quizEntities;
    private List<QuizDto> quizDtos;

    @BeforeEach
    void initializeService() {
        quizService = new QuizServiceImpl(quizRepository, mapper);
    }

    // NOTE: This will be slow to setup each time, but it will refresh the data which we want
    @BeforeEach
    void setupData() {
        ObjectMapper om = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .addModule(new Jdk8Module())
                .addModule(new ParameterNamesModule())
                .build();
        File entsFile = new File("src/test/resources/mockQuizEntities.json");
        File dtosFile = new File("src/test/resources/mockQuizDtos.json");
        try {
            quizEntities = om.readValue(entsFile, new TypeReference<List<Quiz>>(){});
            quizDtos = om.readValue(dtosFile, new TypeReference<List<QuizDto>>(){});
        } catch(Exception exc) { // Catching general exception only b/c in unit test
            throw new IllegalArgumentException("Unable to start QuizServiceTest: " + exc);
        }
    }

    @Test
    void findOwnerQuizzes_shouldSucced() {
        final String owner = quizEntities.get(0).getOwner();
        when(quizRepository.findByOwner(owner)).thenReturn(quizEntities);
        QuizListDto dtos = quizService.findOwnerQuizzes(owner);
        assertEquals(quizEntities.size(), dtos.getQuizzes().size());
    }

    @Test
    void findQuizByNameAndOwner_shouldSucceed() {
        final Quiz quiz = quizEntities.get(0);
        when(quizRepository.findByNameAndOwner(quiz.getName(), quiz.getOwner())).thenReturn(quizEntities);
        QuizListDto dtos = quizService.findQuizByNameAndOwner(quiz.getOwner(), quiz.getName());
        assertEquals(quizEntities.size(), dtos.getQuizzes().size());
    }

    @Test
    void findQuizById_shouldSucceed() {
        final Quiz quiz = quizEntities.get(0);
        when(quizRepository.findById(quiz.getId())).thenReturn(Optional.of(quiz));
        QuizDto dto = quizService.findQuizById(quiz.getId());
        assertNotNull(dto);
        assertEquals(quiz.getId(), dto.getId());
    }

    @Test
    void findQuizById_notFound() {
        final Quiz quiz = quizEntities.get(0);
        when(quizRepository.findById(quiz.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.findQuizById(quiz.getId()));
    }

    @Test
    void deleteQuiz_shouldSucceed() {
        final String id = quizEntities.get(0).getId();
        when(quizRepository.existsById(id)).thenReturn(true);
        quizService.deleteQuiz(id);
    }

    @Test
    void deleteQuiz_notFound() {
        final String id = quizEntities.get(0).getId();
        when(quizRepository.existsById(id)).thenReturn(false);
        assertThrows(QuizServiceException.class, () -> quizService.deleteQuiz(id));
    }

    @Test
    void addQuiz_shouldSucceed() {
        final QuizDto dto = quizDtos.get(0);
        // Empty values that wouldn't be set when adding quiz dto
        dto.setId(null);
        dto.setCreated(null);
        dto.setLastUpdated(null);

        when(quizRepository.existsByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(false);
        when(quizRepository.insert(any(Quiz.class))).thenReturn(quizEntities.get(0));
        final QuizDto returned = quizService.addQuiz(dto);
        assertNotNull(returned.getId());
        assertEquals(dto.getName(), returned.getName());
        assertEquals(dto.getOwner(), returned.getOwner());
        assertEquals(dto.getQuestions().size(), returned.getQuestions().size());
    }

    @Test
    void addQuiz_invalidQuizName() {
        final QuizDto dto = quizDtos.get(0);
        // Empty values that wouldn't be set when adding quiz dto
        dto.setId(null);
        dto.setCreated(null);
        dto.setLastUpdated(null);

        // Set invalid name
        dto.setName("=NOT VALID=");
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.addQuiz(dto));
    }

    @Test
    void addQuiz_quizExists() {
        final QuizDto dto = quizDtos.get(0);
        // Empty values that wouldn't be set when adding quiz dto
        dto.setId(null);
        dto.setCreated(null);
        dto.setLastUpdated(null);

        when(quizRepository.existsByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(true);
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.addQuiz(dto));
    }

    @Test
    void updateQuiz_shouldSucceed() {
        final QuizDto dto = quizDtos.get(0);
        final Quiz ent = quizEntities.get(0);

        when(quizRepository.existsById(dto.getId())).thenReturn(true);
        when(quizRepository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        when(quizRepository.save(any())).thenReturn(ent);
        final QuizDto returned = quizService.updateQuiz(dto.getId(), dto);
        assertNotNull(returned.getId());
        assertEquals(dto.getName(), returned.getName());
        assertEquals(dto.getOwner(), returned.getOwner());
        assertEquals(dto.getQuestions().size(), returned.getQuestions().size());
    }

    @Test
    void updateQuiz_notFound() {
        final QuizDto dto = quizDtos.get(0);
        when(quizRepository.existsById(dto.getId())).thenReturn(false);
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.updateQuiz(dto.getId(), dto));
    }

    @Test
    void updateQuiz_invalidName() {
        final QuizDto dto = quizDtos.get(0);
        // Make name invalid
        dto.setName("_NAME_");
        when(quizRepository.existsById(dto.getId())).thenReturn(true);
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.updateQuiz(dto.getId(), dto));
    }

    @Test
    void updateQuiz_duplicateName() {
        final QuizDto dto = quizDtos.get(0);
        final Quiz ent = quizEntities.get(0);
        // Update entity so name matches, but ids do not
        ent.setName(dto.getName());
        ent.setId(dto.getId() + "0");

        when(quizRepository.existsById(dto.getId())).thenReturn(true);
        when(quizRepository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        Assertions.assertThrows(QuizServiceException.class, () -> quizService.updateQuiz(dto.getId(), dto));
    }
}
