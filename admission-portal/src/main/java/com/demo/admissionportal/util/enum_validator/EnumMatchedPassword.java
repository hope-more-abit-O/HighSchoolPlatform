package com.demo.admissionportal.util.enum_validator;

import com.demo.admissionportal.util.impl.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumMatchedPassword {
    String message() default "Mật khẩu không khớp ! ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
