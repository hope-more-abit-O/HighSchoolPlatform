package com.demo.admissionportal.constants;

public enum UniversityCampusStatus {
    ACTIVE("Hoạt động"),

    INACTIVE("Không hoạt động");

    public final String name;

    UniversityCampusStatus(String name) {
        this.name = name;
    }
}