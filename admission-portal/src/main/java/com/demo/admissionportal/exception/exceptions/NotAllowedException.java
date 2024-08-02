package com.demo.admissionportal.exception.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Not Allowed exception.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Data
public class NotAllowedException extends RuntimeException{
    private Map<String, String> errors;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public NotAllowedException(String message) {
        super(message);
    }

    public NotAllowedException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
