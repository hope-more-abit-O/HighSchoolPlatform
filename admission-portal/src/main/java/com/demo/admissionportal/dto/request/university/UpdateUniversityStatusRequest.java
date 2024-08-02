package com.demo.admissionportal.dto.request.university;

import com.demo.admissionportal.constants.AccountStatus;

public record UpdateUniversityStatusRequest(String note, AccountStatus status) {
}
