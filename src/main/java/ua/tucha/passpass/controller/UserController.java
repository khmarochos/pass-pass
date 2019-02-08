package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.service.UserService;
import ua.tucha.passpass.util.RouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

@Slf4j
@Controller
@RequestMapping(RouteRegistry.UserRouteRegistry.FIRST_LEVEL + "/*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    ViewSelector viewSelector;

    @GetMapping(RouteRegistry.UserRouteRegistry.SIGN_UP)
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        log.debug("User is {}", user);
        return(viewSelector.selectView("/user/sign-up"));
    }

    @PostMapping(RouteRegistry.UserRouteRegistry.SIGN_UP)
    public String registration(@ModelAttribute("user") @Validated(User.UserValidationGroup.class) User user) {
        log.debug("User is {}", user);
        userService.newUser(user);
        return "redirect:" + RouteRegistry.UserRouteRegistry.SIGN_UP;
    }

}
