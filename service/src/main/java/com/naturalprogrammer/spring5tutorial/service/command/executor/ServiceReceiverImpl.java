package com.naturalprogrammer.spring5tutorial.service.command.executor;

import com.naturalprogrammer.spring5tutorial.service.command.Command;
import com.naturalprogrammer.spring5tutorial.service.command.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

@Component
public class ServiceReceiverImpl  implements ServiceReceiver, ApplicationContextAware{

    private static final Logger LOG = LoggerFactory.getLogger(ServiceReceiver.class);
    private ApplicationContext context;

////    @Override
//    public <OUT, IN extends Request> OUT doRequest(Class<? extends Command<IN,OUT>> commandClazz, IN request) {
//        Command<IN,OUT> command = context.getBean(commandClazz);
//        if ( command != null){
//
//            return command.execute(request);
//        }
//        throw new NoSuchBeanDefinitionException("Unable to find suitable command for request and command class");
//    }

    @Override
    public <OUT, IN extends Request> OUT doRequest(String commandBeanName, IN request, Errors errors) {
        Object command = context.getBean(commandBeanName);
        if ( command != null){
            ((Command<IN,OUT>)command).setErrors(errors);
            LOG.debug("Executing command {}", commandBeanName);
            return ((Command<IN,OUT>)command).execute(request);
        }
        throw new NoSuchBeanDefinitionException("Unable to find suitable command for request and command class");
    }

    @Override
    public <OUT, IN extends Request> OUT doRequest(String commandBeanName, IN request) {
        return doRequest(commandBeanName,request,null);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Assert.notNull(applicationContext,"Application context can not be null");
        this.context = applicationContext;
    }
}
