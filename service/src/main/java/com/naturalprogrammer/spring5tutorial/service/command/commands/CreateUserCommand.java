package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ResendVerificationCodeRequest;
import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import java.util.UUID;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;

@Component
public class CreateUserCommand extends BaseCommand<UserRequest, User>{
    private static final Logger log = LoggerFactory.getLogger(CreateUserCommand.class);

    @Autowired
    private UserService userService;



    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean validate(UserRequest request){
        return validate(request,UserRequest.SignupValidation.class);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    protected User proceedToExecute(UserRequest request) {

        User user = request.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(User.Role.UNVERIFIED);
        user.setVerificationCode(UUID.randomUUID().toString());

        User finalUser = userRepository.save(user);
        MyUtils.afterCommit(() -> {

            MyUtils.login(finalUser);
            boolean success = true;
            try {
                userService.sendVerificationMail(user);
            }
            catch (MessagingException msg){
                success = false;
            }
            if (!success){
                log.warn("Sending verification mail to "
                    + finalUser.getEmail() + " failed");
            }
        });
        return finalUser;
    }
}
