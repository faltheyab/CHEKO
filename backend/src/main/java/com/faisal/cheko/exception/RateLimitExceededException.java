package com.faisal.cheko.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final String clientIp;
    private final int retryAfterSeconds;


    public RateLimitExceededException(String message, String clientIp, int retryAfterSeconds) {
        super(message);
        this.clientIp = clientIp;
        this.retryAfterSeconds = retryAfterSeconds;
    }


    public String getClientIp() {
        return clientIp;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}

