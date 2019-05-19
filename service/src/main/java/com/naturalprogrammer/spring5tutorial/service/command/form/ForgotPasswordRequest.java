package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;
import com.naturalprogrammer.spring5tutorial.service.validation.EmailExists;

public class ForgotPasswordRequest implements Request {
	
	@EmailExists
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getObjectName() {
		return "forgotPassword";
	}
}
