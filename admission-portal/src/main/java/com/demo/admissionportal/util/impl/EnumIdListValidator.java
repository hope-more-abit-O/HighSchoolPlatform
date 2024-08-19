package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumId;
import com.demo.admissionportal.util.enum_validator.EnumIdList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumIdListValidator implements ConstraintValidator<EnumIdList, List<Integer>> {
    @Override
    public boolean isValid(List<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {
        if (ids == null) {
            return true;
        }
        if (ids.isEmpty()) {
            return false;
        }
        List<Integer> falseIds = new ArrayList<>();
        ids.forEach(id -> {if(id <=0) falseIds.add(id);});
        if (falseIds.isEmpty()) {
            return true;
        }
        return false;
    }
}
