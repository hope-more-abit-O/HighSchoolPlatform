package com.demo.admissionportal.constants;

public enum AdmissionUpdateStatus {
    PENDING("Hoạt động"),
    ACCEPTED("Không hoạt động"),
    DENIED("Chờ duyệt"),
    EXPIRED("Hết hạn");
    public final String name;

    AdmissionUpdateStatus(String name) {
        this.name = name;
    }
}
