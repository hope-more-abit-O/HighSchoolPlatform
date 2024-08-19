package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumQuota;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumQuotaValidator implements ConstraintValidator<EnumQuota, Integer> {
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null)
            return true;
        return integer >= 0;
    }
}
