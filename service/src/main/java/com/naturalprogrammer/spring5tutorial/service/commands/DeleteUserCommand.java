package com.naturalprogrammer.spring5tutorial.service.commands;

import javax.validation.constraints.AssertTrue;

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
