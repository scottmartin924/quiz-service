package com.quizling.quizservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
public abstract class BaseEntity {
    protected Instant created;
    protected Instant lastUpdated;
}
