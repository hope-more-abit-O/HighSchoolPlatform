package com.demo.admissionportal.dto.response;


import com.demo.admissionportal.constants.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
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

    public static ResponseData<?> error(String message, List<String> errorsMessage) {
        Map<String, String> errors = new HashMap<>();
        for (String error : errorsMessage) {
            errors.put("Error detail", error);
        }
        return new ResponseData<>(ResponseCode.C201.getCode(), message, errors);
    }
    public static <T> ResponseData<T> error(String message) {
        Map<String, String> errors = new HashMap<>();
        return new ResponseData<>(ResponseCode.C201.getCode(), message);
    }

    public static ResponseData<?> error(String message, String errorMessage) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error detail", errorMessage);
        return new ResponseData<>(ResponseCode.C201.getCode(), message, errors);
    }

    public static <T> ResponseData<T> ok(String message, T data) {
        return new ResponseData<>(ResponseCode.C200.getCode(), message, data);
    }
}
