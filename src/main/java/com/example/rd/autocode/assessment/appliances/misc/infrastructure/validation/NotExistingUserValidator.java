package com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation;

import com.example.rd.autocode.assessment.appliances.user.client.signUp.SignUpClientForm;
import com.example.rd.autocode.assessment.appliances.user.login.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotExistingUserValidator implements ConstraintValidator<NotExistingUser, SignUpClientForm> {
    private final UserRepository userRepository;

    public NotExistingUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(SignUpClientForm form, ConstraintValidatorContext context) {
        if (userRepository.existsByEmail(form.getEmail())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{UserAlreadyExisting}")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
