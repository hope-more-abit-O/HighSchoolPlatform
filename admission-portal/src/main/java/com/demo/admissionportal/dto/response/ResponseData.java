package com.demo.admissionportal.dto.response;


import com.demo.admissionportal.constants.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

/**
 * The type Response data.
 *
 * @param <T> the type parameter
 */
@Data
public class ResponseData<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     */
//PUT, PATCH, DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param data    the data
     */
//GET, POST
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new Response data.
     *
     * @param status  the status
     * @param message the message
     * @param errors  the errors
     */
    public ResponseData(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static ResponseData<?> error(String message, Map<String, String> errors) {
        return new ResponseData<>(ResponseCode.C201.getCode(), message, errors);
    }
}
