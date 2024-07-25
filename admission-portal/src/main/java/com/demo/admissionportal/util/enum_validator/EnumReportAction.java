package com.demo.admissionportal.util.enum_validator;

import com.demo.admissionportal.util.impl.ReportActionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReportActionValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumReportAction {
    String message() default "Chỉ cho phép hành động 'KHÔNG' và 'XÓA'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}