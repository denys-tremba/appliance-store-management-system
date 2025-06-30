package com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidatorPassayAdapter implements ConstraintValidator<Password, String> {
    @Value("${validation.password.length.min}")
    private int minLength;
    @Value("${validation.password.length.max}")
    private int maxLength;
    @Value("${validation.password.character.uppercase}")
    private int uppercaseChars;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(
                new LengthRule(minLength, maxLength),
                new CharacterRule(EnglishCharacterData.UpperCase, uppercaseChars)
        );

        RuleResult result = validator.validate(new PasswordData(value));

        if (!result.isValid()) {
            context.disableDefaultConstraintViolation();
            String template = String.join(" ", validator.getMessages(result));
            context.buildConstraintViolationWithTemplate(template)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
