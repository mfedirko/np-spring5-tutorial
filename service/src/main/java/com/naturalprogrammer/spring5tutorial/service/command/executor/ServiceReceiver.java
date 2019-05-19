package com.naturalprogrammer.spring5tutorial.service.command.executor;

import com.naturalprogrammer.spring5tutorial.service.command.Command;
import com.naturalprogrammer.spring5tutorial.service.command.Request;
import org.springframework.validation.Errors;

public interface ServiceReceiver {
//    <OUT, IN extends Request> OUT doRequest(Class<? extends Command<IN,OUT>> commandClazz, IN request);
    <OUT, IN extends Request> OUT doRequest(String commandBeanName, IN request);
    <OUT, IN extends Request> OUT doRequest(String commandBeanName, IN request, Errors errors);

}
