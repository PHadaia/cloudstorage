package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public LoginController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping()
    public String loginView(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("signupSuccess", true);
        return "login";
    }

    @PostMapping()
    public String loginUser(@ModelAttribute("user") User user, Model model, Authentication authentication) {
        String loginError = null;

        if(userService.getUserByUsername(user.getUsername()) == null) {
            loginError = "User does not exist";
        }

        if(loginError == null) {
            if(authenticationService.authenticate(authentication) == null) {
                loginError = "Incorrect password";
            }
        }

        if(loginError == null) {
            model.addAttribute("loginSuccess", true);
        } else {
            model.addAttribute("loginError", loginError);
        }

        return "login";
    }
}
