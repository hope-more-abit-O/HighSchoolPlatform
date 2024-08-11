package com.demo.admissionportal.constants;

public enum AdmissionScoreStatus {
    EMPTY ("Chưa cập nhập điểm"),
    COMPLETE ("Đã có điểm"),
    PARTIAL ("Đang cập nhập điểm"),;
    public String name;

    AdmissionScoreStatus(String name) {
        this.name = name;
    }
}
