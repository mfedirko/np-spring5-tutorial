package com.naturalprogrammer.spring5tutorial.service.command;

import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest;
import com.naturalprogrammer.spring5tutorial.service.exception.ServiceValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

public abstract class BaseCommand<IN extends Request,OUT> implements Command<IN, OUT> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCommand.class);
    private ThreadLocal<Errors> errors = new ThreadLocal<>();

    protected boolean throwValidationExceptionOnError = true;

    @Autowired
    @Qualifier("defaultValidator")
    protected SmartValidator validator;


    protected Errors getErrors(IN request){
        Errors localError = errors.get();
        if (localError == null) {
            return new BeanPropertyBindingResult(request, request.getObjectName());
        }
        return localError;
    }

    protected boolean validate(IN request){
        Errors errors = getErrors(request);
        validator.validate(request,errors);
        if (errors.hasErrors()){
            if (throwValidationExceptionOnError) {
                throw new ServiceValidationException(errors);
            }
            return false;
        }
        return true;
    }
    protected boolean validate(IN request, Object ... groups){
        Errors errors = getErrors(request);
        validator.validate(request,errors, groups);
        if (errors.hasErrors()) {
            if (throwValidationExceptionOnError) {
                throw new ServiceValidationException(errors);
            }
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OUT execute(IN request) {
        if (validate(request)) {
            setErrors(null);
            return proceedToExecute(request);
        }
        return null;
    }
    protected abstract OUT proceedToExecute(IN request);

    @Override
    public void setErrors(Errors errors) {
        this.errors.set(errors);
        if (errors != null) setThrowValidationExceptionOnError(false);
    }

    public void setThrowValidationExceptionOnError(boolean throwValidationExceptionOnError) {
        if (errors == null) LOG.debug("Cannot disable validation exceptions when errors is null");
        else {
            this.throwValidationExceptionOnError = throwValidationExceptionOnError;
        }
    }
}
