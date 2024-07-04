package com.demo.admissionportal.util.impl;


import com.demo.admissionportal.util.enum_validator.EnumStaffUsernameValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class StaffUsernameValidatorImpl implements ConstraintValidator<EnumStaffUsernameValidator, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^staff\\d+$");


    @Override
    public void initialize(EnumStaffUsernameValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
}