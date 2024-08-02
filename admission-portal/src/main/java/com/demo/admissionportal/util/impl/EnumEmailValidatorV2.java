package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumEmailV2;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Enum email validator v2.
 */
public class EnumEmailValidatorV2 implements ConstraintValidator<EnumEmailV2, String> {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) {
            return true;
        }
        return email.matches(EMAIL_REGEX);
    }
}
