package com.demo.admissionportal.constants;

import lombok.Getter;

/**
 * Enumeration representing the different status of an account.
 *
 * @author duyhieu
 * @version 1.1
 * @last-updater hopeless
 * @since 11 /06/2024
 */
@Getter
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
    private final String name;

    AccountStatus(String name) {
        this.name = name;
    }

}