package com.demo.admissionportal.constants;

import lombok.Getter;

public enum AdmissionStatus {
    PENDING ("Đang chờ"),
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động"),
    STAFF_INACTIVE ("Không hoạt động (Nhân viên)");
    public String name;

    AdmissionStatus(String name) {
        this.name = name;
    }
}