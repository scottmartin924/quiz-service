package com.quizling.quizservice.error;

import lombok.Getter;

/**
 * Custom exception to handle errors in QuizService
 */
@Getter
public class QuizServiceException extends RuntimeException {
    private ErrorStatus status;

    public QuizServiceException(String message, ErrorStatus status) {
        super(message);
        this.status = status;
    }
}
