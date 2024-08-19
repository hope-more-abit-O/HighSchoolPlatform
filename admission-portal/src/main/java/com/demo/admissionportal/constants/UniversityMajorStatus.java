package com.demo.admissionportal.constants;

public enum UniversityMajorStatus {

    ACTIVE("Hoạt động"),

    INACTIVE("Không hoạt động");

    public final String name;

    UniversityMajorStatus(String name) {
        this.name = name;
    }
}
