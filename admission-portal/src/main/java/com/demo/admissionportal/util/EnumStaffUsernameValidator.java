package com.demo.admissionportal.util;

import com.demo.admissionportal.util.impl.StaffUsernameValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StaffUsernameValidatorImpl.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumStaffUsernameValidator {
    String message() default "Tên đăng nhập không hợp lệ. Định dạng phải là staff + số";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
