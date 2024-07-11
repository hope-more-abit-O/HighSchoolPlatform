package com.demo.admissionportal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@Getter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class StoreDataFailedException extends RuntimeException{
    private Map<String, String> errors;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public StoreDataFailedException(String message) {
        super(message);
    }

    public StoreDataFailedException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
