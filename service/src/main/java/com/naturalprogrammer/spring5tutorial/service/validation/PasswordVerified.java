package com.naturalprogrammer.spring5tutorial.service.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;

import org.hibernate.validator.constraints.NotBlank;

@Constraint(validatedBy={PasswordVerifiedValidator.class})
@NotBlank
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface PasswordVerified {
    String message() default "{invalidPassword}";

    Class[] groups() default {};

    Class[] payload() default {};
}

