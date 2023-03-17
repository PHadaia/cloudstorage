package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView(@ModelAttribute("user") User user) {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        String signupError = null;

        if(!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "Username already exists";
        }

        if(signupError == null) {
            int userId = userService.createUser(user);
            if(userId < 0) {
                signupError = "Error during signup";
            }
        }

        if(signupError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }

        return "redirect:/login";
    }
}
