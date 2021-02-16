package com.quizling.quizservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizServiceApplication {
	// TODO Setup routs, Add unit tests for service and full integration tests with controller, look into HATEOAS w/ reactive
	public static void main(String[] args) {
		SpringApplication.run(QuizServiceApplication.class, args);
	}
}
