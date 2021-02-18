package com.quizling.quizservice.service;

import com.quizling.quizservice.dto.QuizDto;
import com.quizling.quizservice.dto.QuizListDto;
import com.quizling.quizservice.entity.Quiz;
import com.quizling.quizservice.error.ErrorMessage;
import com.quizling.quizservice.error.ErrorStatus;
import com.quizling.quizservice.error.QuizServiceException;
import com.quizling.quizservice.mapper.Mapper;
import com.quizling.quizservice.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {
    final private QuizRepository quizRepository;
    final private Mapper<QuizDto, Quiz> quizMapper;

    final private Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9 ]*");

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository, Mapper<QuizDto, Quiz> quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }

    @Override
    public QuizDto addQuiz(QuizDto dto) {
        String quizName = dto.getName();
        // Check name is allowed
        if (!isNameValid(quizName)) {
            throw new QuizServiceException(
              ErrorMessage.bind(ErrorMessage.NAME_VALIDATION_ERROR, quizName), ErrorStatus.VALIDATTION_ERROR);
        }

        // Check name/owner combo unique
        if (quizRepository.existsByNameAndOwner(quizName, dto.getOwner())) {
            throw new QuizServiceException(ErrorMessage.bind(ErrorMessage.QUIZ_EXISTS, quizName, dto.getOwner()), ErrorStatus.VALIDATTION_ERROR);
        }

        final Quiz entity = quizRepository.insert(quizMapper.dtoToEntity(dto));
        return quizMapper.entityToDto(entity);
    }

    @Override
    public QuizListDto findOwnerQuizzes(String owner) {
        final List<QuizDto> dtos = quizRepository.findByOwner(owner)
                .stream()
                .map(quizMapper::entityToDto)
                .collect(Collectors.toList());
        return new QuizListDto(dtos);
    }

    @Override
    public QuizDto findQuizById(String id) {
        return quizRepository.findById(id)
                .map(quizMapper::entityToDto)
                .orElseThrow(() -> new QuizServiceException(ErrorMessage.bind(ErrorMessage.NOT_FOUND, id), ErrorStatus.NOT_FOUND));
    }

    @Override
    public QuizListDto findQuizByNameAndOwner(String owner, String name) {
        final List<QuizDto> dtos = quizRepository.findByNameAndOwner(name, owner)
                .stream()
                .map(quizMapper::entityToDto)
                .collect(Collectors.toList());
        return new QuizListDto(dtos);
    }

    @Override
    public QuizDto updateQuiz(String id, QuizDto dto) {
        // Check quiz with id exists
        if (!quizRepository.existsById(id)) {
            throw new QuizServiceException(ErrorMessage.bind(ErrorMessage.NOT_FOUND, id), ErrorStatus.NOT_FOUND);
        }

        // Check name valid
        if (!isNameValid(dto.getName())) {
            throw new QuizServiceException(
                    ErrorMessage.bind(ErrorMessage.NAME_VALIDATION_ERROR, dto.getName()), ErrorStatus.VALIDATTION_ERROR);
        }

        // Check quiz name/owner unique (find quiz by name and owner and if not this quiz id then throw error
        final List<Quiz> existingQuiz = quizRepository.findByNameAndOwner(dto.getName(), dto.getOwner());
        if (!existingQuiz.isEmpty() && !existingQuiz.get(0).getId().equals(id)) {
            throw new QuizServiceException(ErrorMessage.bind(ErrorMessage.QUIZ_EXISTS, dto.getName(), dto.getOwner()), ErrorStatus.VALIDATTION_ERROR);
        }

        final Quiz updatedEnt = quizRepository.save(quizMapper.dtoToEntity(dto));
        return quizMapper.entityToDto(updatedEnt);
    }

    @Override
    public void deleteQuiz(String id) {
        // Check quiz with id exists
        if (!quizRepository.existsById(id)) {
            throw new QuizServiceException(ErrorMessage.bind(ErrorMessage.NOT_FOUND, id), ErrorStatus.NOT_FOUND);
        }
        quizRepository.deleteById(id);
    }

    /**
     * Confirm quiz name is "valid"...basically alphanumeric with spaces for now
     */
    private boolean isNameValid(String quizName) {
        return quizName != null && NAME_PATTERN.matcher(quizName).matches();
    }
}
