package com.faisal.cheko.exception;

import java.time.LocalDateTime;
import java.util.Map;


public class ValidationErrorResponse {

    private final int status;
    private final String message;
    private final Map<String, String> errors;
    private final LocalDateTime timestamp;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}