package com.demo.admissionportal.constants;

public enum ActionStatus {

    APPROVE("Đồng ý"),
    REJECT("Từ chối"),
    RECHECK("Kiểm tra lại");
    public final String name;

    ActionStatus(String name) {
        this.name = name;
    }
}
