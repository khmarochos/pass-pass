package ua.tucha.passpass.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.util.RouteRegistry;

@Controller
@RequestMapping(RouteRegistry.UserRouteRegistry.FIRST_LEVEL + "/*")
public class UserController {

    @GetMapping(RouteRegistry.UserRouteRegistry.SIGN_UP)
    public String registration(Model model) {
        return("sign-up");
    }

}
