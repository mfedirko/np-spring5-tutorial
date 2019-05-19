package com.naturalprogrammer.spring5tutorial.web.controllers;

import com.naturalprogrammer.spring5tutorial.service.command.commands.ConfirmVerificationCodeCommand;
import com.naturalprogrammer.spring5tutorial.service.command.commands.SendVerificationCodeCommand;
import com.naturalprogrammer.spring5tutorial.service.command.commands.UpdateUserCommand;
import com.naturalprogrammer.spring5tutorial.service.command.executor.ServiceReceiver;
import com.naturalprogrammer.spring5tutorial.service.command.form.ConfirmVerificationCodeRequest;
import com.naturalprogrammer.spring5tutorial.service.command.form.ResendVerificationCodeRequest;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest;
import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest.UpdateValidation;
import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private ServiceReceiver serviceExecutor;
	@Autowired
	private UserService userService;

	@GetMapping("/{verificationCode}/verify")
	public String verify(@PathVariable String verificationCode,
			RedirectAttributes redirectAttributes) {
		ConfirmVerificationCodeRequest req = new ConfirmVerificationCodeRequest();
		req.setVerificationCode(verificationCode);
		Boolean success = serviceExecutor.doRequest("confirmVerificationCodeCommand",req);
		if (success) {
			MyUtils.flash(redirectAttributes, "success", "verificationSuccessful");
		}
		return "redirect:/";
	}

	@GetMapping("/{userId}/resend-verification-mail")
	public String resendVerificationMail(@PathVariable("userId") User user,
			RedirectAttributes redirectAttributes) throws MessagingException {
		ResendVerificationCodeRequest req = new ResendVerificationCodeRequest();
		boolean success =serviceExecutor.doRequest("sendVerificationCodeCommand", req);
		if (success) {
			MyUtils.flash(redirectAttributes, "success", "verificationMailResent");
		}
		return "redirect:/";
	}

	@GetMapping("/{userId}")
	public String getById(@PathVariable Long userId, Model model) {

		model.addAttribute(userService.fetchById(userId));
		return "user";
	}

	@GetMapping("/{userId}/edit")
	public String edit(@PathVariable("userId") User user, Model model) {
		model.addAttribute(user);
		return "user-edit";
	}

	@PostMapping("/{userId}/edit")
	public String update(@PathVariable("userId") User oldUser,
//			@Validated(UpdateValidation.class)
			@ModelAttribute("user") UserRequest userCommand,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		userCommand.setOriginalUserID(oldUser.getId());
		User u = serviceExecutor.doRequest("updateUserCommand",userCommand,result);
		if (result.hasErrors())
			return "user-edit";
		MyUtils.flash(redirectAttributes, "success", "updateUserSuccess");
		return "redirect:/";
	}

}
