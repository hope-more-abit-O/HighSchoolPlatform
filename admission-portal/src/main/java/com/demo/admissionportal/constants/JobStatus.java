package com.demo.admissionportal.constants;

/**
 * The enum Job status.
 */
public enum JobStatus {
    /**
     * Active job status.
     */
    ACTIVE("Hoạt động"),
    /**
     * The Inactive.
     */
    INACTIVE("Không hoạt động");

    /**
     * The Name.
     */
    public String name;

    JobStatus(String name) {
        this.name = name;
    }
}
