package com.demo.admissionportal.constants;

/**
 * Enumeration representing the different types of universities.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
public enum UniversityType {
    /**
     * Public university.
     */
    PUBLIC("Công lập"),
    /**
     * Private university.
     */
    PRIVATE("Tư thục"),
    /**
     * Military university.
     */
    MILITARY("Quân đội");

    public final String name;

    UniversityType(String name) {
        this.name = name;
    }
}