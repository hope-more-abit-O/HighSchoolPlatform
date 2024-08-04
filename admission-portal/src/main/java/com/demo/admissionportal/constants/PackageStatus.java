package com.demo.admissionportal.constants;

/**
 * The enum Package status.
 */
public enum PackageStatus {
    /**
     * Active package status.
     */
    ACTIVE("Hoạt động"),
    /**
     * The Inactive.
     */
    INACTIVE("Không hoạt động"),
    /**
     * The Complete.
     */
    COMPLETE("Hoàn thành");

    public String name;

    PackageStatus(String name) {
        this.name = name;
    }
}
