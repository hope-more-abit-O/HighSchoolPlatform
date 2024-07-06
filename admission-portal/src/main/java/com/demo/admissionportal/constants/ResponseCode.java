package com.demo.admissionportal.constants;

/**
 * The enum Response code.
 */
public enum ResponseCode {
    /**
     * C 200 response code.
     */
    C200(1, "SUCCESSFULLY"),
    /**
     * C 201 response code.
     */
    C201(2, "FAILED"),
    /**
     * The C 203.
     */
    C203(3, "NOT FOUND"),
    /**
     * C 204 response code.
     */
    C204(4, "EXISTED"),
    /**
     * The C 205.
     */
    C205(5, "BAD REQUEST"),

    /**
     * C 206 response code.
     */
    C206(6, "CREATED"),

    /**
     * C 207 response code.
     */
    C207(7, "INTERNAL_SERVER_ERROR"),

    /**
     * C 208 response code.
     */
    C208(8, "UNSUPPORTED OPERATION");
    private int code;
    private String message;

    /**
     * Instantiates a new Response code.
     *
     * @param code    the code
     * @param message the message
     */
    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}