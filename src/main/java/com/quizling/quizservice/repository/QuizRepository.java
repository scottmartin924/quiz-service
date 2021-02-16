package com.quizling.quizservice.repository;


import com.quizling.quizservice.entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring data Quiz repository
 */
@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

    /**
     * Find quiz with the given name and owner
     *
     * @param name the quiz name
     * @param owner the quiz owner
     * @return find quizzes with given name with given owner
     */
    List<Quiz> findByNameAndOwner(String name, String owner);

    /**
     * Find all quizzes with a given owner.
     *
     * @param owner the owner
     * @return all quizzes belonging to the given owner
     */
    List<Quiz> findByOwner(String owner);

    /**
     * Find all quizzes with given name.
     *
     * @param name the name of the quiz
     * @return list of quizzes of that name
     */
    List<Quiz> findByName(String name);

    /**
     * Determine if Quiz exists with given name and owner
     *
     * @param name the name
     * @param owner the owner
     * @return true if quiz owned by given owner with given name exists else false
     */
    boolean existsByNameAndOwner(String name, String owner);
}
