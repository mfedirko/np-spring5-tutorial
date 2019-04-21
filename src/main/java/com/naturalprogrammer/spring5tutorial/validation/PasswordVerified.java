package com.naturalprogrammer.spring5tutorial.validation;

import com.naturalprogrammer.spring5tutorial.commands.HasPassword;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

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

