package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.Normalizer;

public class EnumIdValidator implements ConstraintValidator<EnumId, Integer> {
    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) {
            return true;
        }
        if (id < 0) {
            return false;
        }
        return true;
    }
}