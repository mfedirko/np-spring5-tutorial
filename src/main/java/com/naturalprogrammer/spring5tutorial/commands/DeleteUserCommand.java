package com.naturalprogrammer.spring5tutorial.commands;

import com.naturalprogrammer.spring5tutorial.commands.ConfirmPasswordCommand;
import com.naturalprogrammer.spring5tutorial.commands.HasPassword;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.validation.Password;
import com.naturalprogrammer.spring5tutorial.validation.PasswordVerified;
import javax.validation.constraints.AssertTrue;
import org.hibernate.validator.constraints.NotEmpty;

public class DeleteUserCommand  extends ConfirmPasswordCommand{

    public static interface DeleteConfirmedStep {}

    @AssertTrue(message = "{deleteUser.confirm.error}", groups = DeleteConfirmedStep.class)
    private boolean confirmDelete;


    public boolean isConfirmDelete() {
        return confirmDelete;
    }

    public void setConfirmDelete(boolean confirmDelete) {
        this.confirmDelete = confirmDelete;
    }
}
