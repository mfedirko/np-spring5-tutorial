package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.ConfirmPasswordRequest;
import com.naturalprogrammer.spring5tutorial.service.command.form.DeleteUserRequest;
import com.naturalprogrammer.spring5tutorial.service.exception.ServiceValidationException;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Component
public class DeleteUserCommand extends BaseCommand<DeleteUserRequest,Boolean>{

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected Boolean proceedToExecute(DeleteUserRequest request) {
        //Need to get the user to delete from the user logged in
        User currentUser = MyUtils.currentUser()
                .orElseThrow(() -> {
                    Errors errors = getErrors(request);
                    errors.reject("user.not.loggedin","No logged in user found");
                    return new ServiceValidationException(errors);
                });
        userRepository.delete(currentUser);
        MyUtils.afterCommit(() -> MyUtils.logout());
        return true;
    }

    @Override
    protected boolean validate(DeleteUserRequest request){
        return validate(request,
                DeleteUserRequest.DeleteConfirmedStep.class,
                ConfirmPasswordRequest.PasswordConfirmedStep.class);
    }

}
