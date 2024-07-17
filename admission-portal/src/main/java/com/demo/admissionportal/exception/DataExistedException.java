package com.demo.admissionportal.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Data
public class DataExistedException extends RuntimeException{
    private Map<String, String> errors;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public DataExistedException(String message) {
        super(message);
    }

    public DataExistedException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
