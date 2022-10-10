package com.rmmcosta.superduperdrive.controller;

import com.rmmcosta.superduperdrive.model.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String getLoginPage(@ModelAttribute("user") LoginForm loginForm, Model model) {
        return "login";
    }
}
