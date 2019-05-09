package com.naturalprogrammer.spring5tutorial.service.commands;

import com.naturalprogrammer.spring5tutorial.service.validation.PasswordVerified;

public class ConfirmPasswordCommand implements HasPassword {

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
