package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.integration.mail.MailSender;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ResendVerificationCodeRequest;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import static com.naturalprogrammer.spring5tutorial.service.utils.MyUtils.isAdminOrSelfLoggedIn;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

@Component
public class SendVerificationCodeCommand extends BaseCommand<ResendVerificationCodeRequest,Boolean>{
    @Value("${applicationUrl}") private String applicationUrl;

    @Autowired
    private MailSender mailSender;


    @Autowired
    private UserService userService;

    @Override
    protected boolean validate(ResendVerificationCodeRequest req){
        User user = MyUtils.currentUser().orElse(null);
        MyUtils.validate(user != null, "userNotFound");
        MyUtils.validate(isAdminOrSelfLoggedIn(user), "notPermitted");
        MyUtils.validate(user.getRoles().contains(User.Role.UNVERIFIED),
                "alreadyVerified");
        return true;
    }



    @Override
    protected Boolean proceedToExecute(ResendVerificationCodeRequest request) {
        try {
            sendVerificationMail(MyUtils.currentUser().get());
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    private void sendVerificationMail(User user) throws  javax.mail.MessagingException {
        userService.sendVerificationMail(user);
    }

//    private void resendVerificationMail(User user) throws MessagingException {
//
////        MyUtils.validate(user != null, "userNotFound");
////        MyUtils.validate(isAdminOrSelfLoggedIn(user), "notPermitted");
////        MyUtils.validate(user.getRoles().contains(User.Role.UNVERIFIED),
////                "alreadyVerified");
//
//        sendVerificationMail(user);
//    }
}
