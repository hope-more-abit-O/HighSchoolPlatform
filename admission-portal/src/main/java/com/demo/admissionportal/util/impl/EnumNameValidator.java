package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Enum name validator.
 */
public class EnumNameValidator implements ConstraintValidator<EnumName, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
