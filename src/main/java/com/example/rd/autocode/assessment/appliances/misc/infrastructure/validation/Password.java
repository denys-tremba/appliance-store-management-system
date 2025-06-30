package com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordValidatorPassayAdapter.class)
public @interface Password {
    String message() default "{PasswordViolation}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
