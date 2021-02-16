package com.quizling.quizservice.error;

/**
 * Class to encapsulate error messages...not great just quickly thrown together
 */
public class ErrorMessage {
    public static final ErrorMessage QUIZ_EXISTS = new ErrorMessage("Quiz name %s for user %s already exists!");
    public static final ErrorMessage NOT_FOUND = new ErrorMessage("Quiz id %s not found!");
    public static final ErrorMessage NOT_FOUND_NAME_OWNER = new ErrorMessage("Quiz name %s with owner %s was not found!");
    public static final ErrorMessage NAME_VALIDATION_ERROR = new ErrorMessage("Quiz name %s invalid. Quiz names must be non-empty and alphanumeric.");

    final String message;
    private ErrorMessage(String msg) {
        this.message = msg;
    }

    /**
     * Bind the string error message to the parameters
     * @param msg the error message
     * @param params the parameters to include in the message
     * @return the string for the error message
     */
    public static String bind(ErrorMessage msg, String... params) {
        return String.format(msg.message, params);
    }
}
