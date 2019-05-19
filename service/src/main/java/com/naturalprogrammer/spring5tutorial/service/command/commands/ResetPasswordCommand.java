package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.integration.mail.MailSender;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ResetPasswordRequest;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import com.naturalprogrammer.spring5tutorial.service.validation.Password;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ResetPasswordCommand extends BaseCommand<ResetPasswordRequest,User> {

    private UserRepository userRepository;
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ResetPasswordCommand(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender){
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected User proceedToExecute(ResetPasswordRequest request) {
        Optional<User> user = userRepository
                .findByResetPasswordCode(request.getResetPasswordCode());

        MyUtils.validate(user.isPresent(), "wrongResetPasswordCode");
        User u = user.get();
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setResetPasswordCode(null);

        userRepository.save(u);
        return u;
    }
}
