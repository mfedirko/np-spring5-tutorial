package com.naturalprogrammer.spring5tutorial.service.command.commands;

import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.BaseCommand;
import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import static com.naturalprogrammer.spring5tutorial.service.utils.MyUtils.isAdminOrSelfLoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUserCommand extends BaseCommand<UserRequest, User> {
    @Override
    protected boolean validate(UserRequest request) {
        return validate(request,UserRequest.UpdateValidation.class);
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    protected User proceedToExecute(UserRequest request) {
        User oldUser = userRepository.findById(request.getOriginalUserID())
                .orElse(null);

        MyUtils.validate(oldUser != null, "userNotFound");
            MyUtils.validate(isAdminOrSelfLoggedIn(oldUser), "notPermitted");

            oldUser.setName(request.getName());

            User currentUser = MyUtils.currentUser().get();
            if (currentUser.getRoles().contains(User.Role.ADMIN))
                oldUser.setRoles(request.getRoles());

            userRepository.save(oldUser);

            MyUtils.afterCommit(() -> {
                if (currentUser.getId().equals(oldUser.getId()))
                    MyUtils.login(oldUser);
            });
        return currentUser;
    }
}
