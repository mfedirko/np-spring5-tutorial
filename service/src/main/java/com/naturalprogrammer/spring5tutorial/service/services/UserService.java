package com.naturalprogrammer.spring5tutorial.service.services;

import com.naturalprogrammer.spring5tutorial.service.command.commands.DeleteUserCommand;
import javax.mail.MessagingException;

//import org.springframework.boot.context.event.ApplicationReadyEvent;


import com.naturalprogrammer.spring5tutorial.domain.User;

public interface UserService {

//	void afterApplicationReady(ApplicationReadyEvent event);
//	void resendVerificationMail(User user) throws MessagingException;
	User fetchById(Long userId);
	boolean verifyPassword(String rawPass);
	void sendVerificationMail(User u) throws  javax.mail.MessagingException;
//	void mailResetPasswordLink(User user);
}
