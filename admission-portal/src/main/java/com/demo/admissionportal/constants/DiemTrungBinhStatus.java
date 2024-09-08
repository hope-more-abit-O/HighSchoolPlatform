package com.demo.admissionportal.constants;

public enum DiemTrungBinhStatus {
    GiamManh("GIẢM MẠNH"),
    Giam("GIẢM"),
    GiamNhe("GIẢM NHẸ"),
    KhongDoi("KHÔNG ĐỔI"),
    TangNhe("TĂNG NHẸ"),
    Tang("TĂNG"),
    TangManh("TĂNG MẠNH");

    private final String name;

    DiemTrungBinhStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}