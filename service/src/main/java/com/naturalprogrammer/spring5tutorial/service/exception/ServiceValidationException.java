package com.naturalprogrammer.spring5tutorial.service.exception;

import javax.xml.ws.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

public class ServiceValidationException extends RuntimeException {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceValidationException.class);
    private Errors errors;

    public ServiceValidationException(Errors errors){
        super();
        Assert.notNull(errors,"Must have non-null errors");
        this.errors = errors;
        LOG.debug("service validation failed {}",errors);
    }

    public Errors getErrors() {
        return errors;
    }
}
