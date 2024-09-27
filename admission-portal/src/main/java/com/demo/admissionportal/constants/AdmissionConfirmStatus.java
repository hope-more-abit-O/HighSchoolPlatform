package com.demo.admissionportal.constants;

public enum AdmissionConfirmStatus {
    PENDING ("Đang chờ"),
    CONFIRMED ("Đã xác nhận"),
    REJECTED ("Đã từ chối"),
    UPDATE_PENDING("Chờ cập nhập"),
    UPDATE_CANCEL("Huỷ cập nhập"),
    UPDATE_DENIED("Từ chối cập nhập"),
    STAFF_INACTIVE ("Bị dừng hoạt động bởi nhân viên"),
    STAFF_UPDATED("Đã cập nhập sang đề án khác (Nhân viên)"),
    UPDATE_EXPIRED("Hết hạn cập nhập");
    public String name;

    AdmissionConfirmStatus(String name) {
        this.name = name;
    }
}
