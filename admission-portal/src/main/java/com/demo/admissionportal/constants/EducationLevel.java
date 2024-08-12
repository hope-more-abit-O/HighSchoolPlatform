package com.demo.admissionportal.constants;

/**
 * The enum Education level.
 */
public enum EducationLevel {
    /**
     * Secondary education level.
     */
    /**
     * High education level.
     */
    HIGH("Học sinh"),
    /**
     * Other education level.
     */
    OTHER("Phụ huynh");
    /**
     * The Name.
     */
    public final String name;

    EducationLevel(String name) {
        this.name = name;
    }
}