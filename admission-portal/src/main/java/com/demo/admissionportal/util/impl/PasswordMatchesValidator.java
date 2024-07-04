package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.dto.request.resetPass.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.util.enum_validator.EnumMatchedPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<EnumMatchedPassword, ConfirmResetPasswordRequest> {

    @Override
    public void initialize(EnumMatchedPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(ConfirmResetPasswordRequest request, ConstraintValidatorContext context) {
        boolean isValid = request.getNewPassword().equals(request.getConfirmNewPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Mật khẩu không khớp !")
                    .addPropertyNode("confirmNewPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}