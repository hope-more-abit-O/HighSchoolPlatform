package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumReportAction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ReportActionValidator implements ConstraintValidator<EnumReportAction, String> {

    private final List<String> allowedActions = Arrays.asList("NONE", "DELETE");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return allowedActions.contains(value);
    }
}