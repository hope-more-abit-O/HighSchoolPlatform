package com.demo.admissionportal.constants;

public enum MethodStatus {
    ACTIVE("Hoạt động"),
    INACTIVE("Không hoạt động");
    public final String name;

    MethodStatus(String name) {
        this.name = name;
    }
}
