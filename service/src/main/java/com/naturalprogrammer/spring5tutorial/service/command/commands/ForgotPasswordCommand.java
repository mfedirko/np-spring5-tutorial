package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.integration.mail.MailSender;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ForgotPasswordRequest;
import com.naturalprogrammer.spring5tutorial.service.exception.ServiceValidationException;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import java.util.UUID;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Component
public class ForgotPasswordCommand extends BaseCommand<ForgotPasswordRequest,Boolean>{

    @Autowired
    private MailSender mailSender;

    @Value("${applicationUrl}") private String applicationUrl;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserService userService;
    private static Logger log = LoggerFactory.getLogger(ForgotPasswordCommand.class);

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    protected Boolean proceedToExecute(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->{
                    String email = request.getEmail();
                    Errors errors = getErrors(request);
                    if (email == null){
                        errors.reject("user.email.null","User not found - Email is null");
                    }
                    else {
                        errors.reject("user.notfound","User not found with email " + email);
                    }
                    return new ServiceValidationException(errors);
                });
        user.setResetPasswordCode(UUID.randomUUID().toString());
        userRepository.save(user);
        MyUtils.afterCommit(() -> mailResetPasswordLink(user));
        return true;
    }


    public void mailResetPasswordLink(User user) {

        String resetPasswordLink = applicationUrl + "/reset-password/" +
                user.getResetPasswordCode();

        try {
            mailSender.send(user.getEmail(), MyUtils.getMessage("resetPasswordSubject"),
                    MyUtils.getMessage("resetPasswordBody","", resetPasswordLink));
        } catch (MessagingException e) {
            log.warn("Error sending reset password mail to " + user.getEmail(), e);
        }
    }

}
