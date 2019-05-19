package com.naturalprogrammer.spring5tutorial.web.controllers.exception;

import com.naturalprogrammer.spring5tutorial.service.exception.GeneralServiceException;
import com.naturalprogrammer.spring5tutorial.service.exception.ServiceValidationException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    // from spring boot AbstractErrorController
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
    @ExceptionHandler(ServiceValidationException.class)
    public ModelAndView handleServiceValidationException(ServiceValidationException ex,
                                                         HttpServletRequest req){
        ModelAndView mav = defaultErrorHandler(req,ex);
        mav.getModelMap().addAttribute("error",ex.getErrors());
        mav.addObject("status",400);

        return mav;
    }
    @ExceptionHandler(GeneralServiceException.class)
    public ModelAndView handleServiceValidationException(GeneralServiceException ex,
                                                         HttpServletRequest req){
        ModelAndView mav = defaultErrorHandler(req,ex);
        mav.getModelMap().addAttribute("error",ex.getLocalizedMessage());
        mav.addObject("status",400);

        return mav;
    }
    @ExceptionHandler( NoHandlerFoundException.class)
    public ModelAndView notFound(NoHandlerFoundException ex) throws Exception {
        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("status",404);
        mav.addObject("message", "Page Not found");
        mav.addObject("error", "Not Found");
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;

    }

    private static RuntimeException blankException(){
        return new RuntimeException("");
    }

    @ExceptionHandler(value = Throwable.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Throwable e)  {

        HttpStatus status = getStatus(req);
        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("status",status.value());
        mav.addObject("message", (Optional.ofNullable(e).orElse(blankException())).getMessage());
        mav.addObject("error", Optional.ofNullable(status.getReasonPhrase()).orElse(e.getClass().getSimpleName()));
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}
