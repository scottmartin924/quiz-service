package com.quizling.quizservice.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper from dto to entity and vice versa
 * Used to convert from dtos to entities
 *
 * @param <S> the dto type
 * @param <T> the entity
 */
public interface Mapper<S,T> {

    /**
     * Convert entity to dto
     * @param entity the entity to convert
     * @return the converted dto
     */
    S entityToDto(T entity);

    /**
     * Convert dto to entity
     * @param dto the dto to convert
     * @return the entity
     */
    T dtoToEntity(S dto);

    /**
     * Convert list of entities to list of dtos
     * @param entities the entities to convert
     * @return list of converted dtos
     */
    default List<S> entityToDto(List<T> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * convert list of dtos to list of entities
     * @param dtos the dtos to convert
     * @return list of converted entities
     */
    default List<T> dtoToEntity(List<S> dtos) {
        return dtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
