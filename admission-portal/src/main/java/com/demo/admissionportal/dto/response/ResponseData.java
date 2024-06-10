package com.demo.admissionportal.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
}
