package com.quizling.quizservice.web;

import com.quizling.quizservice.dto.ErrorDto;
import com.quizling.quizservice.error.QuizServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class QuizServiceExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(QuizServiceExceptionHandler.class);

    /**
     * Handle custom exceptions
     * @param exc the quiz service exception
     * @return response entity
     */
    @ExceptionHandler({ QuizServiceException.class })
    public ResponseEntity<?> handleException(QuizServiceException exc) {
        LOG.debug("Handling web service exception: ", exc);
        HttpStatus status;
        switch(exc.getStatus()) {
            case VALIDATTION_ERROR:
                status = HttpStatus.BAD_REQUEST;
                break;
            case NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        final ErrorDto dto = ErrorDto.builder()
                .message(exc.getMessage())
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(dto);
    }
}
