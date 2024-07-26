package com.demo.admissionportal.exception.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class QueryException extends RuntimeException {
    private Map<String, String> errors;
    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}