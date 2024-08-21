package com.demo.admissionportal.constants;

public enum UniversityTrainingProgramStatus {

    ACTIVE("Hoạt động"),

    INACTIVE("Không hoạt động");

    public final String name;

    UniversityTrainingProgramStatus(String name) {
        this.name = name;
    }
}
