package com.naturalprogrammer.spring5tutorial.service.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;

@Component
public class UniqueEmailValidator
	implements ConstraintValidator<UniqueEmail, String> {
	
	private UserRepository userRepository;
	
	public UniqueEmailValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void initialize(UniqueEmail arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext arg1) {
		
		return !userRepository.findByEmail(email).isPresent();
	}
}
