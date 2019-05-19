package com.naturalprogrammer.spring5tutorial.service.validation;

import com.naturalprogrammer.spring5tutorial.service.command.form.ResetPasswordRequest;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.naturalprogrammer.spring5tutorial.service.command.commands.ResetPasswordCommand;

@Component
public class RetypePasswordValidator
	implements ConstraintValidator<RetypePassword, ResetPasswordRequest> {
	
	@Override
	public void initialize(RetypePassword arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(ResetPasswordRequest resetPasswordCommand, ConstraintValidatorContext context) {
		
		if (Objects.equals(resetPasswordCommand.getPassword(),
				resetPasswordCommand.getRetypePassword()))
			return true;
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("{passwordsDoNotMatch}")
			.addPropertyNode("retypePassword").addConstraintViolation();
		
		return false;
	}
}
