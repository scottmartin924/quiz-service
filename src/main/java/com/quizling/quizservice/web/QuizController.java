package com.quizling.quizservice.web;

import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.dto.QuizListDto;
import com.quizling.quizservice.error.QuizServiceException;
import com.quizling.quizservice.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class QuizController {
    final private QuizService quizService;

    @Autowired
    public QuizController(QuizService qService) {
        this.quizService = qService;
    }

    /**
     * Add quiz
     * @param dto the dto of the quiz to add
     * @return server response
     */
    @PostMapping("/{user}/quiz")
    public ResponseEntity<QuizDto> addQuiz(@PathVariable("user") String ownerId, @RequestBody QuizDto dto) {
        dto.setOwner(ownerId);
        // FIXME Should actually return a 201 and the URI (not sure best way to keep track of the URI)
        return ResponseEntity.ok(quizService.addQuiz(dto));
    }

    /**
     * Update quiz
     * @param quizId the id of the quiz to update
     * @param dto the value to update the quiz to
     * @return server response
     */
    @PutMapping("/{user}/quizzes/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable("quizId") String quizId, @RequestBody QuizDto dto) {
        return ResponseEntity.ok(quizService.updateQuiz(quizId, dto));
    }

    /**
     * Delete a quiz
     * @param quizId the id of the quiz to delete
     * @return server response
     */
    @DeleteMapping("/{user}/quizzes/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable("quizId") String quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find all quizzes for a given owner
     * @param owner the owner to find quizzes for
     * @return a server response
     */
    @GetMapping("/{user}/quizzes")
    public ResponseEntity<?> findOwnerQuizzes(@PathVariable("user") String owner, @RequestParam("name") String name) {
        QuizListDto foundQuizzes;
        if (name != null) {
            foundQuizzes = quizService.findQuizByNameAndOwner(owner, name);
        } else {
            foundQuizzes = quizService.findOwnerQuizzes(owner);
        }
        return ResponseEntity.ok(foundQuizzes);
    }

    /**
     * Find quiz by id
     * @param quizId the quiz id
     * @return a server response
     */
    @GetMapping("/{user}/quizzes/{quizId}")
    public ResponseEntity<?> findQuiz(@PathVariable("quizId") String quizId) {
        return ResponseEntity.ok(quizService.findQuizById(quizId));
    }
}
