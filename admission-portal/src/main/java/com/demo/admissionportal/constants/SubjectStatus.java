package com.demo.admissionportal.constants;


/**
 * The enum Subject status.
 */
public enum SubjectStatus {
    /**
     * Active subject status.
     */
    ACTIVE("Hoạt động"),
    /**
     * Inactive subject status.
     */
    INACTIVE("Không hoạt động");
    public final String name;

    SubjectStatus(String name) {
        this.name = name;
    }
}