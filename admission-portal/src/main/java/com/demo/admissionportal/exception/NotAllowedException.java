package com.demo.admissionportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Not Allowed exception.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotAllowedException extends RuntimeException{
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public NotAllowedException(String message) {
        super(message);
    }
}
