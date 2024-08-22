package com.demo.admissionportal.constants;

public enum AdmissionConfirmStatus {
    PENDING ("Đang chờ"),
    CONFIRMED ("Đã xác nhận"),
    REJECTED ("Đã từ chối"),
    STAFF_INACTIVE ("Bị dừng hoạt động bởi nhân viên");
    public String name;

    AdmissionConfirmStatus(String name) {
        this.name = name;
    }
}
