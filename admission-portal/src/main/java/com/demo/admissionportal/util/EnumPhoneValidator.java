package com.demo.admissionportal.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Enum phone validator.
 */
public class EnumPhoneValidator implements ConstraintValidator<EnumPhone, String> {
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (phone == null) {
            return false;
        }
        return phone.matches("^0\\d{9,10}$");
    }
}
