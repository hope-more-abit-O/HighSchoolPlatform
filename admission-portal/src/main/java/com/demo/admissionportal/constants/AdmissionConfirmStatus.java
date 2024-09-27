package com.demo.admissionportal.constants;

public enum AdmissionConfirmStatus {
    PENDING ("Đang chờ"),
    CONFIRMED ("Đã xác nhận"),
    REJECTED ("Đã từ chối"),
    UPDATE_PENDING("Chờ cập nhật"),
    UPDATE_CANCEL("Huỷ cập nhật"),
    UPDATE_DENIED("Từ chối cập nhật"),
    STAFF_INACTIVE ("Bị dừng hoạt động bởi nhân viên"),
    STAFF_UPDATED("Đã cập nhật sang đề án khác (Nhân viên)"),
    UPDATE_EXPIRED("Hết hạn cập nhật");
    public String name;

    AdmissionConfirmStatus(String name) {
        this.name = name;
    }
}
