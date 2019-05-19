package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;

public class ResendVerificationCodeRequest implements Request{
    @Override
    public String getObjectName() {
        return "resendVerificationCode";
    }
}
