package com.demo.admissionportal.constants;

/**
 * The enum Like status.
 */
public enum LikeStatus {
    /**
     * Like like status.
     */
    LIKE("Thích"),
    /**
     * Unlike like status.
     */
    UNLIKE("Bỏ thích");
    /**
     * The Name.
     */
    public String name;

    LikeStatus(String name) {
        this.name = name;
    }
}
