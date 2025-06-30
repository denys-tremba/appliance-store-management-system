package com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation;

import com.example.rd.autocode.assessment.appliances.user.manageAccount.EnterPasswordForm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, EnterPasswordForm> {
    @Override
    public boolean isValid(EnterPasswordForm value, ConstraintValidatorContext context) {
        if (!value.getPassword().equals(value.getPasswordRepeated())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{PasswordsNotMatching}")
                    .addPropertyNode("passwordRepeated")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
