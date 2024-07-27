package com.demo.admissionportal.constants;

/**
 * The enum Post properties status.
 */
public enum PostPropertiesStatus {
    /**
     * Active post properties status.
     */
    ACTIVE("Hoạt động"),
    /**
     * Inactive post properties status.
     */
    INACTIVE("Không hoạt động");

    /**
     * -- GETTER --
     *  Gets name.
     *
     * @return the name
     */
    public final String name;

    PostPropertiesStatus(String name) {
        this.name = name;
    }
}
