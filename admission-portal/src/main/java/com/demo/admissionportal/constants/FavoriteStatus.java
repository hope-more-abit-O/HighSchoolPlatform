package com.demo.admissionportal.constants;

/**
 * The enum Favorite status.
 */
public enum FavoriteStatus {
    /**
     * The Follow.
     */
    FOLLOW("Theo dõi"),
    /**
     * The Unfollow.
     */
    UNFOLLOW("Huỷ theo dõi");

    /**
     * The Name.
     */
    public String name;

    FavoriteStatus(String name) {
        this.name = name;
    }
}
