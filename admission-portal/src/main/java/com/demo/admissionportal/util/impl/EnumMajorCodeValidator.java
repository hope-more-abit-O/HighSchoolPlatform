package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumMajorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumMajorCodeValidator implements ConstraintValidator<EnumMajorCode, String> {
    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (code.length() < 3 || code.length() > 9) {
            return false;
        }
        for (char c : code.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
