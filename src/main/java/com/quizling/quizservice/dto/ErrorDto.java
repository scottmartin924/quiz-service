package com.quizling.quizservice.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorDto {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
}
