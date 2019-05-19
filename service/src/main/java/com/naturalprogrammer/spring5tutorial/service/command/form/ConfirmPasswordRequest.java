package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;
import com.naturalprogrammer.spring5tutorial.service.validation.PasswordVerified;

public class ConfirmPasswordRequest implements HasPassword, Request {

    @Override
    public String getObjectName() {
        return "confirmPassword";
    }

    public static interface PasswordConfirmedStep {}
    @Override
    public String getPassword() {
        return password;
    }

    @PasswordVerified(groups = PasswordConfirmedStep.class )
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

}
