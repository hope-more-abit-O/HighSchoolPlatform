package com.demo.admissionportal.constants;

public enum AdmissionUpdateStatus {
    PENDING("Chờ duyệt"),
    ACCEPTED("Đã được chấp nhận"),
    DENIED("Đã bị từ chối"),
    UNIVERSITY_DELETED("Đã xóa"),
    EXPIRED("Hết hạn");
    public final String name;

    AdmissionUpdateStatus(String name) {
        this.name = name;
    }
}
