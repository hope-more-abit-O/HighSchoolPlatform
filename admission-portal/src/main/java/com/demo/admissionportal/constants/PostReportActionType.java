package com.demo.admissionportal.constants;

public enum PostReportActionType {
    DELETE("XÓA"),
    NONE("KHÔNG");

    public final String name;

    PostReportActionType(String name) {
        this.name = name;
    }
}
