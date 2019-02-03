package ua.tucha.passpass.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserRegistrationController {
    @GetMapping("/user/registration")
    public String registration(Model model) {
        return("registration");
    }
}
