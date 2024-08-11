package com.demo.admissionportal.constants;

public enum AdmissionScoreStatus {
    EMPTY ("Chưa cập nhật điểm"),
    COMPLETE ("Đã có điểm"),
    PARTIAL ("Đang cập nhật điểm"),;
    public String name;

    AdmissionScoreStatus(String name) {
        this.name = name;
    }
}
