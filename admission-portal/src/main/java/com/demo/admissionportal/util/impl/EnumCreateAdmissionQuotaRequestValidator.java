package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EnumCreateAdmissionQuotaRequestValidator implements ConstraintValidator<EnumCreateAdmissionQuotaRequest, List<CreateAdmissionQuotaRequest>> {
    @Override
    public boolean isValid(List<CreateAdmissionQuotaRequest> quotaRequests, ConstraintValidatorContext constraintValidatorContext) {
        if (quotaRequests == null || quotaRequests.isEmpty()) {
            return false;
        }
        return true;
    }

//    boolean checkMajorRequestValid()
}
