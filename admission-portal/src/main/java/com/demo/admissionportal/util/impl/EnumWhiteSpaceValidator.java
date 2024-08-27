package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumWhiteSpace;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Enum white space validator.
 */
public class EnumWhiteSpaceValidator implements ConstraintValidator<EnumWhiteSpace, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }
}
