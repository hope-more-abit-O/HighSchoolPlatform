package com.demo.admissionportal.constants;

public enum AdmissionConfirmStatus {
    PENDING ("Đang chờ"),
    CONFIRMED ("Đã xác nhận"),
    REJECTED ("Đã từ chối");
    public String name;

    AdmissionConfirmStatus(String name) {
        this.name = name;
    }
}
