package com.rmmcosta.superduperdrive.controller;

import com.rmmcosta.superduperdrive.model.SignupForm;
import com.rmmcosta.superduperdrive.model.User;
import com.rmmcosta.superduperdrive.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void afterConstruct() {
        modelMapper = new ModelMapper();
    }

    @GetMapping
    public String getSignupPage(@ModelAttribute("user") SignupForm signupForm, Model model) {
        return "/signup";
    }
    @PostMapping
    public String doSignup(@ModelAttribute("user") SignupForm signupForm, Model model) {
        System.out.println("Create User");
        userService.createUser(modelMapper.map(signupForm, User.class));
        return "redirect:/login";
    }
}
