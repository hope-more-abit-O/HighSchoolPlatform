package com.demo.admissionportal.constants;

import lombok.Getter;

@Getter
public enum Region {
    NORTH("Miền Bắc"),
    CENTRAL("Miền Trung"),
    SOUTH("Miền Nam");

    public final String name;

    private Region(String name) {
        this.name = name;
    }
}
