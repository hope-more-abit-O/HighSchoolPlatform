package com.demo.admissionportal.constants;

/**
 * The enum Response code.
 */
public enum ResponseCode {
    /**
     * C 200 response code.
     */
    C200(200, "SUCCESSFULLY"),
    /**
     * C 201 response code.
     */
    C201(500, "FAILED"),
    /**
     * The C 203.
     */
    C203(404, "NOT FOUND"),
    /**
     * C 204 response code.
     */
    C204(500, "EXISTED"),
    /**
     * The C 205.
     */
    C205(400, "BAD REQUEST"),

    /**
     * C 206 response code.
     */
    C206(200, "CREATED"),

    /**
     * C 207 response code.
     */
    C207(500, "INTERNAL_SERVER_ERROR"),

    /**
     * C 208 response code.
     */
    C208(500, "UNSUPPORTED OPERATION"),
    /**
     * C 209 response code.
     */
    C209(403, "UNAUTHORIZED"),

    /**
     * The C 210.
     */
    C210(500, "TOO MANY REQUEST");
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