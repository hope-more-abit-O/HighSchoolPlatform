package com.demo.admissionportal.constants;

/**
 * The enum Gender.
 */
public enum Gender {
    /**
     * Male gender.
     */
    MALE("NAM"),
    /**
     * Female gender.
     */
    FEMALE("NỮ"),
    /**
     * Other gender.
     */
    OTHER("KHÁC");
    public String name;

    Gender(String name) {
        this.name = name;
    }
}