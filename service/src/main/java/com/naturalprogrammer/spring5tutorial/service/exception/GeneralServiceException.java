package com.naturalprogrammer.spring5tutorial.service.exception;

public class GeneralServiceException extends RuntimeException {
    public GeneralServiceException(String msg){
        super(msg);
    }
}
