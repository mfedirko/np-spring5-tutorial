package com.naturalprogrammer.spring5tutorial.service.command.form;

import com.naturalprogrammer.spring5tutorial.service.command.Request;
import javax.validation.constraints.AssertTrue;

public class DeleteUserRequest extends ConfirmPasswordRequest{

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
