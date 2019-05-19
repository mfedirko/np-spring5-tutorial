package com.naturalprogrammer.spring5tutorial.service.services;

import com.naturalprogrammer.spring5tutorial.service.command.commands.DeleteUserCommand;
import static com.naturalprogrammer.spring5tutorial.service.utils.MyUtils.isAdminOrSelfLoggedIn;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.mail.MessagingException;
import javax.validation.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.naturalprogrammer.spring5tutorial.service.command.commands.ForgotPasswordCommand;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.domain.User.Role;
import com.naturalprogrammer.spring5tutorial.integration.mail.MailSender;
import com.naturalprogrammer.spring5tutorial.dao.repositories.UserRepository;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;

@Service("userService")
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public class UserServiceImpl implements UserService {


	@Autowired
	@Qualifier("defaultValidator")
	private Validator validator;

	private static Log log = LogFactory.getLog(UserServiceImpl.class);
	
	@Value("${application.admin.email:admin@example.com}")
	private String adminEmail;
	
	@Value("${application.admin.name:First Admin}")
	private String adminName;

	@Value("${application.admin.password:password}")
	private String adminPassword;

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private MailSender mailSender;
	private String applicationUrl;

	public UserServiceImpl(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			MailSender mailSender,
			@Value("${applicationUrl}") String applicationUrl) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
		this.applicationUrl = applicationUrl;
	}
	
	@PostConstruct
	public void init() {
		
		log.info("Inside Post construct");
		initUsers();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void initUsers(){
		if (!userRepository.findByEmail(adminEmail).isPresent()) {
			User u = new User();
			u.setEmail(adminEmail);
			u.setName(adminName);
			u.setId(1L);
			u.setRoles(Collections.singleton(Role.ADMIN));
			u.setPassword(passwordEncoder.encode(adminPassword));
			User saved = userRepository.save(u);
			if (saved != null) log.debug("Saved admin user successfully");
			else log.debug("Failed to save initial admin user");
		}

	}
	
//	@Override
//	@EventListener
//	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
//	public void afterApplicationReady(ApplicationReadyEvent event) {
//
//		User user = new User();
//
//		if (!userRepository.findByEmail(adminEmail).isPresent()) {
//
//			user.setEmail(adminEmail);
//			user.setName(adminName);
//			user.setPassword(passwordEncoder.encode(adminPassword));
//			user.getRoles().add(Role.ADMIN);
//
//			userRepository.save(user);
//		}
//	}





	@Override
	public boolean verifyPassword(String rawPass){
		User current = MyUtils.currentUser().orElseThrow(() -> new RuntimeException("No user session found"));
		return passwordEncoder.matches(rawPass,current.getPassword());
	}





	public void sendVerificationMail(User user) throws  javax.mail.MessagingException {

		String verificationLink = applicationUrl + "/users/" +
				user.getVerificationCode() + "/verify";

		mailSender.send(user.getEmail(), MyUtils.getMessage("verifySubject"),
				MyUtils.getMessage("verifyBody","", verificationLink));
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly = true)
	public User fetchById(Long userId) {
		
		User user = userRepository.getOne(userId);
		MyUtils.validate(user != null, "userNotFound");
		
		user.setEditable(isAdminOrSelfLoggedIn(user));
		if (!user.isEditable())
			user.setEmail("Confidential");
		
		return user;
	}


}