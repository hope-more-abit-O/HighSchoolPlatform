package com.demo.admissionportal.constants;

import lombok.Getter;

public enum AdmissionStatus {
    PENDING ("Đang chờ"),
    ACTIVE ("Hoạt động"),
    INACTIVE ("Không hoạt động"),
    STAFF_INACTIVE ("Không hoạt động (Nhân viên)"),
    UPDATE_PENDING("Chờ cập nhật"),
    UPDATE_CANCEL("Huỷ cập nhật"),
    UPDATE_DENIED("Từ chối cập nhật"),
    STAFF_UPDATED("Đã cập nhật sang đề án khác (Nhân viên)"),
    UPDATE_EXPIRED("Hết hạn cập nhật");
    public String name;

    AdmissionStatus(String name) {
        this.name = name;
    }
}