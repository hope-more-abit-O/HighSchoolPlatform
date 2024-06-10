package com.demo.admissionportal.constants;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum SubjectStatus {
    @JsonProperty("ACTIVE")
    ACTIVE,
    @JsonProperty("INACTIVE")
    INACTIVE
}

