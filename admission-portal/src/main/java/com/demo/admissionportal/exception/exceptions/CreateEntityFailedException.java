package com.demo.admissionportal.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CreateEntityFailedException extends RuntimeException{
    private Map<String, String> errors;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public CreateEntityFailedException(String message) {
        super(message);
    }

    public CreateEntityFailedException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}