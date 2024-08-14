package com.demo.admissionportal.constants;

/**
 * The enum Question status.
 */
public enum QuestionStatus {
    /**
     * Active question status.
     */
    ACTIVE("Hoạt động"),
    /**
     * Inactive question status.
     */
    INACTIVE("Không hoạt động");

    public String name;

    QuestionStatus(String name) {
        this.name = name;
    }
}
