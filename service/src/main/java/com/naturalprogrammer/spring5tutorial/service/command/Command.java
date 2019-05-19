package com.naturalprogrammer.spring5tutorial.service.command;

import org.springframework.validation.Errors;

public interface Command<IN,OUT> {
    OUT execute(IN request);
    void setErrors(Errors errors);
}
