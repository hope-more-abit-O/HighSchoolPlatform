package com.demo.admissionportal.constants;

public enum ChiTieuStatus {
    GiamManh("GIẢM MẠNH"),
    Giam("GIẢM"),
    GiamNhe("GIẢM NHẸ"),
    KhongDoi("KHÔNG ĐỔI"),
    TangNhe("TĂNG NHẸ"),
    Tang("TĂNG"),
    TangManh("TĂNG MẠNH");

    private final String name;

    ChiTieuStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}