package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.dto.request.admisison.CreateAdmissionQuotaRequest;
import com.demo.admissionportal.util.enum_validator.EnumCreateAdmissionQuotaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EnumCreateAdmissionQuotaRequestValidator implements ConstraintValidator<EnumCreateAdmissionQuotaRequest, List<CreateAdmissionQuotaRequest>> {
    @Override
    public boolean isValid(List<CreateAdmissionQuotaRequest> quotaRequests, ConstraintValidatorContext constraintValidatorContext) {
        for (CreateAdmissionQuotaRequest quotaRequest : quotaRequests) {

        }


        return false;
    }

//    boolean checkMajorRequestValid()
}
