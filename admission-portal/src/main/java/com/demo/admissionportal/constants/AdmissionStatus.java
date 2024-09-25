package com.demo.admissionportal.constants;

import lombok.Getter;

public enum AdmissionStatus {
    PENDING ("Đang chờ"),
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động"),
    STAFF_INACTIVE ("Không hoạt động (Nhân viên)"),
    UPDATE_PENDING("Chờ cập nhập"),
    UPDATE_CANCEL("Huỷ cập nhập"),
    UPDATE_DENIED("Từ chối cập nhập"),
    STAFF_UPDATED("Đã cập nhập sang đề án khác (Nhân viên)");
    public String name;

    AdmissionStatus(String name) {
        this.name = name;
    }
}