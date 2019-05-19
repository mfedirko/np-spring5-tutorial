package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfirmVerificationCodeRequest implements Request {
    @NotEmpty
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String getObjectName() {
        return "confirmVerificationCode";
    }
}
