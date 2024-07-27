package com.demo.admissionportal.constants;

/**
 * Enumeration representing the different status of an account.
 *
 * @author duyhieu
 * @last-updater hopeless
 * @version 1.1
 * @since 11/06/2024
 */
public enum AccountStatus {
    /**
     * Active account status.
     */
    ACTIVE("Hoạt động"),
    /**
     * Inactive account status.
     */
    INACTIVE("Không hoạt động"),
    /**
     * Pending account status.
     */
    PENDING("Chờ duyệt");

    /**
     * -- GETTER --
     *  Gets name.
     *
     * @return the name
     */
    public final String name;

    AccountStatus(String name) {
        this.name = name;
    }
}