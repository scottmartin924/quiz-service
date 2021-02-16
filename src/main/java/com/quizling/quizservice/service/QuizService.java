package com.quizling.quizservice.service;

import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.dto.QuizListDto;

import java.util.List;
import java.util.Optional;

/**
 * Service to manager Quizzes. All business
 * logic is maintained in this layer
 */
public interface QuizService {

    /**
     * Add quiz
     *
     * @param dto the quiz dto to add
     * @return the newly inserted quiz
     */
    QuizDto addQuiz(QuizDto dto);

    /**
     * Find all quizzes for owner
     * @param owner the owner
     * @return quizzes belonging to the given owner
     */
    QuizListDto findOwnerQuizzes(String owner);

    /**
     * Find quiz with a given id
     * @param id the quiz id
     * @return the quiz or empty mono if not found
     */
    QuizDto findQuizById(String id);

    /**
     * Find quiz with a given name and owner
     *
     * @param owner the owner
     * @param name the quiz name
     * @return quiz or empty mono
     */
    QuizListDto findQuizByNameAndOwner(String owner, String name);

    /**
     * Update a quiz
     * @param id the quiz id to update
     * @param dto the quiz to update
     * @return the newly updated quiz
     */
    QuizDto updateQuiz(String id, QuizDto dto);

    /**
     * Delete quiz with given id
     * @param id the quiz id
     */
    void deleteQuiz(String id);
}
