package ua.tucha.passpass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.util.RouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

@Controller
@RequestMapping(RouteRegistry.UserRouteRegistry.FIRST_LEVEL + "/*")
public class UserController {

    @Autowired
    ViewSelector viewSelector;

    @GetMapping(RouteRegistry.UserRouteRegistry.SIGN_UP)
    public String registration(Model model) throws IllegalAccessException, ClassNotFoundException {
        return(viewSelector.selectView("/user/sign-up"));
    }

}
