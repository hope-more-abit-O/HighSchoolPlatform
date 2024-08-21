package com.demo.admissionportal.constants;

import lombok.Getter;

public enum AdmissionStatus {
    PENDING ("Đang chờ"),
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động");
    public String name;

    AdmissionStatus(String name) {
        this.name = name;
    }
}