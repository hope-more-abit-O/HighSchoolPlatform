package com.demo.admissionportal.constants;

/**
 * The enum Post status.
 */
public enum PostStatus {
    /**
     * Pending post status.
     */
    PENDING("Chờ duyệt"),
    /**
     * Active post status.
     */
    ACTIVE("Hoạt động"),
    /**
     * Private post status.
     */
    PRIVATE("Bị ẩn"),
    /**
     * Inactive post status.
     */
    INACTIVE("Không hoạt động");

    /**
     * -- GETTER --
     *  Gets name.
     *
     * @return the name
     */
    public final String name;

    PostStatus(String name) {
        this.name = name;
    }
}
