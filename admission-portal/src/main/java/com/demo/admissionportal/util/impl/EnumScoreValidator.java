package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumScore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumScoreValidator implements ConstraintValidator<EnumScore, Float> {

    @Override
    public boolean isValid(Float score, ConstraintValidatorContext constraintValidatorContext) {
        if (score == null)
            return true;
        if (score < 0 || score > 50)
            return false;
        return true;
    }
}
