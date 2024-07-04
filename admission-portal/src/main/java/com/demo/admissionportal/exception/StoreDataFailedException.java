package com.demo.admissionportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class StoreDataFailedException extends RuntimeException{
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public StoreDataFailedException(String message) {
        super(message);
    }
}
