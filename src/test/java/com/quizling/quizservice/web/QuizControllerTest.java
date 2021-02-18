package com.quizling.quizservice.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.quizling.quizservice.dto.ErrorDto;
import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.dto.QuizListDto;
import com.quizling.quizservice.entity.Quiz;
import com.quizling.quizservice.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuizControllerTest {

    private static final String MOCK_USER = "scott";
    private static final String USER_BASE_PATH = "/users/%s/quizzes";
    private static final String FORMATTED_PATH = String.format(USER_BASE_PATH, MOCK_USER);

    @MockBean
    private QuizRepository repository;

    private List<Quiz> quizEntities;
    private List<QuizDto> quizDtos;

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
    void findOwnerQuizzes_shouldSucceed(@Autowired WebTestClient client) {
        when(repository.findByOwner(MOCK_USER)).thenReturn(quizEntities);
        client.get().uri(FORMATTED_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuizListDto.class)
                .isEqualTo(new QuizListDto(quizDtos));
    }

    @Test
    void findOwnerQuizzesWithName_shouldSucceed(@Autowired WebTestClient client) {
        final String quizName = "Test Quiz";
        final String path = new StringBuilder(FORMATTED_PATH).append("?name=").append(quizName).toString();
        when(repository.findByNameAndOwner(quizName, MOCK_USER)).thenReturn(quizEntities);
        client.get().uri(path)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuizListDto.class)
                .isEqualTo(new QuizListDto(quizDtos));
    }

    @Test
    void findQuiz_shouldSucceed(@Autowired WebTestClient client) {
        final String quizId = "602c23b9c71eb515503948c2";
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(quizId).toString();
        when(repository.findById(quizId)).thenReturn(Optional.of(quizEntities.get(0)));
        client.get().uri(path)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuizDto.class)
                .isEqualTo(quizDtos.get(0));
    }

    @Test
    void findQuiz_notFound(@Autowired WebTestClient client) {
        final String quizId = "602c23b9c71eb515503948c2";
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(quizId).toString();
        when(repository.findById(quizId)).thenReturn(Optional.empty());
        client.get().uri(path)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorDto.class);
    }

    @Test
    void deleteQuiz_shouldSucceed(@Autowired WebTestClient client) {
        final String quizId = "602c23b9c71eb515503948c2";
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(quizId).toString();
        when(repository.existsById(quizId)).thenReturn(true);
        client.delete().uri(path)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteQuiz_notFound(@Autowired WebTestClient client) {
        final String quizId = "602c23b9c71eb515503948c2";
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(quizId).toString();
        when(repository.existsById(quizId)).thenReturn(false);
        client.delete().uri(path)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorDto.class);
    }

    @Test
    void updateQuiz_shouldSucceed(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(dto.getId()).toString();
        when(repository.existsById(dto.getId())).thenReturn(true);
        when(repository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        when(repository.save(any())).thenReturn(quizEntities.get(0));
        client.put().uri(path)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuizDto.class)
                .isEqualTo(dto);
    }

    @Test
    void updateQuiz_notFound(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(dto.getId()).toString();
        when(repository.existsById(dto.getId())).thenReturn(false);
        client.put().uri(path)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorDto.class);
    }

    @Test
    void updateQuiz_invalidName(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        dto.setName("_INVALID_");
        final String path = new StringBuilder(FORMATTED_PATH).append("/").append(dto.getId()).toString();
        when(repository.existsById(dto.getId())).thenReturn(true);
        when(repository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        when(repository.save(any())).thenReturn(quizEntities.get(0));
        client.put().uri(path)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorDto.class);
    }

    @Test
    void addQuiz_shouldSucceed(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        when(repository.existsByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(false);
        when(repository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        when(repository.insert(any(Quiz.class))).thenReturn(quizEntities.get(0));
        client.post().uri(FORMATTED_PATH)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(QuizDto.class)
                .isEqualTo(quizDtos.get(0));
    }

    @Test
    void addQuiz_invalidName(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        // Clear dto values that wouldn't be there
        dto.setId(null);
        dto.setLastUpdated(null);
        dto.setCreated(null);
        dto.setName("_INVALID_");
        client.post().uri(FORMATTED_PATH)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorDto.class);
    }

    @Test
    void addQuiz_alreadyExists(@Autowired WebTestClient client) {
        final QuizDto dto = quizDtos.get(0);
        // Clear dto values that wouldn't be there
        dto.setId(null);
        dto.setLastUpdated(null);
        dto.setCreated(null);

        when(repository.existsByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(true);
        when(repository.findByNameAndOwner(dto.getName(), dto.getOwner())).thenReturn(quizEntities);
        when(repository.insert(any(Quiz.class))).thenReturn(quizEntities.get(0));
        client.post().uri(FORMATTED_PATH)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorDto.class);
    }
}
