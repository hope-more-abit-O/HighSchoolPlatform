package com.demo.admissionportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataExistedException extends RuntimeException{
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public DataExistedException(String message) {
        super(message);
    }
}
