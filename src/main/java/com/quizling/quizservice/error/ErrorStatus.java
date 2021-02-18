package com.quizling.quizservice.error;

/**
 * Stores error statuses. For the purposes of this simple
 * app this basically maps directly to http statuses, but
 * could be more domain specific in the future
 */
public enum ErrorStatus {
    VALIDATTION_ERROR,
    NOT_FOUND,
    CONFLICT
}
