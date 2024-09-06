package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumMethodCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MethodCodeValidator implements ConstraintValidator<EnumMethodCode, String> {

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (code == null || code.length() < 3 || code.length() > 9) {
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
