package com.naturalprogrammer.spring5tutorial.service.validation;

import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordVerifiedValidator
        implements ConstraintValidator<PasswordVerified, String> {

    UserService userService;

    public PasswordVerifiedValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(PasswordVerified constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.verifyPassword(value);
    }
}