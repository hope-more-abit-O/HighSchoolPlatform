package com.demo.admissionportal.util.enum_validator;

import com.demo.admissionportal.util.impl.EnumEmailValidatorV2;
import com.demo.admissionportal.util.impl.EnumPhoneValidatorV2;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Enum email v2.
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = EnumEmailValidatorV2.class)
public @interface EnumEmailV2 {
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};
}
