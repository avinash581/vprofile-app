package com.visualpathit.account.controller;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.SecurityService;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.validator.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping({"/", "/welcome"})
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false)
                        String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(
            @Valid User userForm,
            BindingResult bindingResult) {

        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);
        securityService.autologin(
                userForm.getUsername(),
                userForm.getPassword());

        return "redirect:/welcome";
    }

    @GetMapping("/index")
    public String index() {
        return "index_home";
    }
}
