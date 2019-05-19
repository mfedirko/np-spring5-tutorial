package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ConfirmVerificationCodeRequest;
import com.naturalprogrammer.spring5tutorial.service.exception.ServiceValidationException;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Component
public class ConfirmVerificationCodeCommand extends BaseCommand<ConfirmVerificationCodeRequest,Boolean>{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    protected Boolean proceedToExecute(ConfirmVerificationCodeRequest request) {
        verify(request);
        return true;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void verify(ConfirmVerificationCodeRequest verificationCode) {

        User currentUser = MyUtils.currentUser().get();

        User user = userRepository.getOne(currentUser.getId());

        MyUtils.validate(user.getRoles().contains(User.Role.UNVERIFIED), "alreadyVerified");
        MyUtils.validate(verificationCode.getVerificationCode().equals(user.getVerificationCode()), "wrongVerificationCode");

        user.getRoles().remove(User.Role.UNVERIFIED);
        user.setVerificationCode(null);

        userRepository.save(user);
        MyUtils.afterCommit(() -> MyUtils.login(user));
    }

}
