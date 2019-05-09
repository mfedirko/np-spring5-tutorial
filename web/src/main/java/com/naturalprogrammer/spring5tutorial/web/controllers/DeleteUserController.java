package com.naturalprogrammer.spring5tutorial.web.controllers;

import com.naturalprogrammer.spring5tutorial.web.aop.NeedsConfirmPassword;
import com.naturalprogrammer.spring5tutorial.service.commands.ConfirmPasswordCommand;
import com.naturalprogrammer.spring5tutorial.service.commands.DeleteUserCommand;
import com.naturalprogrammer.spring5tutorial.service.services.UserService;
import com.naturalprogrammer.spring5tutorial.service.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/delete-account")
@SessionAttributes("deleteForm")
public class DeleteUserController  {
    private static Logger log = LoggerFactory.getLogger(DeleteUserController.class);

    private UserService userService;

    public DeleteUserController(UserService userService){
        this.userService = userService;
    }

    private static final String CONFIRM_DELETE_VIEW = "confirmDelete";
    private static final String CONFIRM_PASS_VIEW = "passwordConfirm";

    @GetMapping
    public String showVerifyPassword(Model model) {
        if (!model.containsAttribute("deleteForm")) {
            model.addAttribute("deleteForm", new DeleteUserCommand());
        }
        return CONFIRM_PASS_VIEW;
    }

    @PostMapping( params = {"step=one"})
    public String stepOneConfirmPassword(@Validated(ConfirmPasswordCommand.PasswordConfirmedStep.class) @ModelAttribute("deleteForm") DeleteUserCommand command,
                                         BindingResult bindingResult, ModelMap modelMap){
        if (bindingResult.hasErrors()){
            return CONFIRM_PASS_VIEW;
        }
        return CONFIRM_DELETE_VIEW;
    }

    @NeedsConfirmPassword
    @PostMapping( params = {"step=confirm"})
    public String confirmDeleteView(@Validated({DeleteUserCommand.DeleteConfirmedStep.class, ConfirmPasswordCommand.PasswordConfirmedStep.class}) @ModelAttribute("deleteForm") DeleteUserCommand deleteUserCommand,
                                    BindingResult bindingResult,
                                    ModelMap modelMap,
                                    RedirectAttributes redirectAttributes,
                                    SessionStatus sessionStatus){
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("errors",bindingResult.getAllErrors());
//            return new RedirectView("/delete-account", false).getUrl();
            return CONFIRM_PASS_VIEW;
        }
        else {
            try {
                userService.deleteUser(deleteUserCommand);
            } catch (Throwable e) {
                e.printStackTrace();
                log.error(e.getMessage());
                MyUtils.flash(redirectAttributes, "danger", "deleteUserFailed", e.getMessage());
                return "redirect:/";
            }
            MyUtils.flash(redirectAttributes, "success", "deleteUserSuccess");
            sessionStatus.setComplete();
            return "redirect:/login";
        }
    }

    @ModelAttribute
    public void setCommonAttrs(Model model){

        //for verify password form
        model.addAttribute("modelAttrNm","deleteForm");
        model.addAttribute("actionUrl","/delete-account?step=one");
    }

}
