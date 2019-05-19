package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;
import com.naturalprogrammer.spring5tutorial.service.validation.Password;
import org.hibernate.validator.constraints.NotEmpty;

//@RetypePassword
public class ResetPasswordRequest implements Request {

	@NotEmpty
	private String resetPasswordCode;

	@Password
	private String password;
	
	@Password
	private String retypePassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	public String getResetPasswordCode() {
		return resetPasswordCode;
	}

	public void setResetPasswordCode(String resetPasswordCode) {
		this.resetPasswordCode = resetPasswordCode;
	}

	@Override
	public String getObjectName() {
		return "resetPassword";
	}
}
