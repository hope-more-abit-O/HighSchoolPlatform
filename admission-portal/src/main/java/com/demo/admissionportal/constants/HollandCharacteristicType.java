package com.demo.admissionportal.constants;

/**
 * The enum Holland characteristic type.
 */
public enum HollandCharacteristicType {
    /**
     * The Realistic.
     */
    Realistic("Thực tế"),
    /**
     * The Investigative.
     */
    Investigative("Nghiên cứu"),
    /**
     * Artistic holland characteristic type.
     */
    Artistic("Nghệ thuật"),
    /**
     * Social holland characteristic type.
     */
    Social("Xã hội"),
    /**
     * The Enterprising.
     */
    Enterprising("Quản lý"),
    /**
     * The Conventional.
     */
    Conventional("Nghiệp vụ");

    /**
     * The Name.
     */
    public String name;

    HollandCharacteristicType(String name) {
        this.name = name;
    }
}
