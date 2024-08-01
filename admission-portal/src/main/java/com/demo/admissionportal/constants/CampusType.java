package com.demo.admissionportal.constants;

/**
 * The enum Campus type.
 */
public enum CampusType {
    /**
     * Headquarters campus type.
     */
    HEADQUARTERS("Cơ sở chính"),
    /**
     * Sub headquarters campus type.
     */
    SUB_HEADQUARTERS("Cơ sở phụ");

    /**
     * -- GETTER --
     * Gets name.
     *
     * @return the name
     */
    public final String name;

    CampusType(String name) {
        this.name = name;
    }
}
