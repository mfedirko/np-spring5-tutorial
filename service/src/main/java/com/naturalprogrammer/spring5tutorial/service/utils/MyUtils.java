package com.naturalprogrammer.spring5tutorial.service.utils;

import java.util.Optional;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring5tutorial.domain.User;

@Component
public class MyUtils {
	
	private static MessageSource messageSource;
	
	public MyUtils(MessageSource messageSource) {
		MyUtils.messageSource = messageSource;
	}

	public static String getMessage(String messageKey, Object... args) {

		return messageSource.getMessage(messageKey, args,
				LocaleContextHolder.getLocale());
	}
    public static String getMessage(String messageKey, String defaultMessage, Object... args) {
        try {
            return messageSource.getMessage(messageKey, args,
                    LocaleContextHolder.getLocale());
        }
        catch (NoSuchMessageException e){
            return String.format(defaultMessage,args);
        }
    }

	public static void validated(Object command, Validator validator) {
		Set<ConstraintViolation<Object>> violations = validator.validate(command);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<Object> constraintViolation : violations) {
				sb.append(constraintViolation.getMessage());
			}
			throw new ConstraintViolationException("Constraint violation: " + sb.toString(), violations);
		}
	}
	
	public static void flash(RedirectAttributes redirectAttributes,
			String flashKind, String flashMessageCode, Object ... args) {

		redirectAttributes.addFlashAttribute("flashMessage",
				getMessage(flashMessageCode,args));
		
		redirectAttributes.addFlashAttribute("flashKind", flashKind);
	}
	
	public static Optional<User> currentUser() {
		
		Authentication auth = SecurityContextHolder
				.getContext().getAuthentication();
		
		if (auth != null) {

			Object principal = auth.getPrincipal();
            System.out.println("Auth NOT null");
			if (principal instanceof User)
				return Optional.of((User) principal);
            System.out.println("Auth principal not instance of User");
            System.out.println(principal);
		}
        System.out.println("Auth is null");
		return Optional.empty();
	}
	
	public static void login(UserDetails user) {
		
		Authentication auth = new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	public static void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	public static void afterCommit(Runnable runnable) {
		
		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronizationAdapter() {
				    @Override
				    public void afterCommit() {

						runnable.run();
				    }
			    }
			);	
	}
	
	public static void validate(boolean valid, String messageKey, Object ... messageArgs) {
		
		if (!valid)
			throw new RuntimeException(MyUtils.getMessage(messageKey,"", messageArgs));
	}
}
