package com.naturalprogrammer.spring5tutorial.web.controllers;

import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.commands.ResetPasswordCommand;
import com.naturalprogrammer.spring5tutorial.service.command.executor.ServiceReceiver;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring5tutorial.service.command.form.ResetPasswordRequest;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;

@Controller
@RequestMapping("/reset-password/{resetPasswordCode}")
public class ResetPasswordController {
	
	private static Log log = LogFactory.getLog(ResetPasswordController.class);
	

	@Autowired
	private ServiceReceiver serviceExecutor;

	@GetMapping
	public String resetPassword(Model model) {
		
		model.addAttribute(new ResetPasswordRequest());
		return "reset-password";
	}	

	@PostMapping
	public String doResetPassword(
			@PathVariable String resetPasswordCode,
			//@Validated
			ResetPasswordRequest resetPasswordRequest,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (!Objects.equals(resetPasswordRequest.getPassword(),
				resetPasswordRequest.getRetypePassword()))
			result.rejectValue("retypePassword", "passwordsDoNotMatch");
		User u =serviceExecutor.doRequest("resetPasswordCommand",resetPasswordRequest,result);
		if (result.hasErrors())
			return "reset-password";
		MyUtils.flash(redirectAttributes, "success", "passwordChanged");
		return "redirect:/login";
	}	
}
