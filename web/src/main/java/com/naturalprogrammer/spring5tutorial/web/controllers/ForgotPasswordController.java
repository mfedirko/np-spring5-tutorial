package com.naturalprogrammer.spring5tutorial.web.controllers;

import com.naturalprogrammer.spring5tutorial.service.command.commands.ForgotPasswordCommand;
import com.naturalprogrammer.spring5tutorial.service.command.executor.ServiceReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring5tutorial.service.command.form.ForgotPasswordRequest;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
	
	private static Log log = LogFactory.getLog(ForgotPasswordController.class);

	@Autowired
	private ServiceReceiver serviceExecutor;


	@GetMapping
	public String forgotPassword(Model model) {
		
		model.addAttribute(new ForgotPasswordRequest());
		return "forgot-password";
	}	

	@PostMapping
	public String doForgotPassword(
			//@Validated
			ForgotPasswordRequest forgotPasswordRequest,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		Boolean success = serviceExecutor.doRequest("forgotPasswordCommand",forgotPasswordRequest,result);
		if (result.hasErrors())
			return "forgot-password";
		if (success) {
			MyUtils.flash(redirectAttributes, "success", "forgotPasswordMailSent");
		}
		return "redirect:/";
	}	
}
