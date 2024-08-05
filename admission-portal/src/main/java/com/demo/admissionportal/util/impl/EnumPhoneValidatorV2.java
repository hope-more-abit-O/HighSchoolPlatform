package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumPhoneV2;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Enum phone validator.
 */
public class EnumPhoneValidatorV2 implements ConstraintValidator<EnumPhoneV2, String> {
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (phone == null) {
            return true;
        }
        return phone.matches("^0\\d{9,10}$");
    }
}