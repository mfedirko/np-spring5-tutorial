package com.naturalprogrammer.spring5tutorial.web.controllers;

import com.naturalprogrammer.spring5tutorial.domain.User;
import com.naturalprogrammer.spring5tutorial.service.command.commands.CreateUserCommand;
import com.naturalprogrammer.spring5tutorial.service.command.executor.ServiceReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest;
import com.naturalprogrammer.spring5tutorial.service.command.form.UserRequest.SignupValidation;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	private static Log log = LogFactory.getLog(SignupController.class);
	
	@Autowired
	private ServiceReceiver serviceExecutor;

	@GetMapping
	public String signup(Model model) {
		
		model.addAttribute("user", new UserRequest());
		return "signup";
	}	

	@PostMapping
	public String doSignup(
			//@Validated(SignupValidation.class)
			@ModelAttribute("user") UserRequest user,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		User u = serviceExecutor.doRequest("createUserCommand",user,result);
		if (result.hasErrors())
			return "signup";
		
		MyUtils.flash(redirectAttributes, "success", "signupSuccess",u.getId());
		return "redirect:/";
	}	
}
