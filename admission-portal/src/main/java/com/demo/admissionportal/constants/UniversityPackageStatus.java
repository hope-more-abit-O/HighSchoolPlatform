package com.demo.admissionportal.constants;

/**
 * The enum University package status.
 */
public enum UniversityPackageStatus {
    /**
     * Active university package status.
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

    UniversityPackageStatus(String name) {
        this.name = name;
    }
}
