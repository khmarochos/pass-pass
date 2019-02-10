package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.service.UserService;
import ua.tucha.passpass.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.util.RouteRegistry.UserRouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

@Slf4j
@Controller
@RequestMapping(UserRouteRegistry.FIRST_LEVEL + "/*")
public class UserController {

    @ModelAttribute("user")
    public User getUser() {
        return new User();
    }


    private final UserService userService;
    private final ViewSelector viewSelector;

    @Autowired
    public UserController(UserService userService, ViewSelector viewSelector) {
        this.userService = userService;
        this.viewSelector = viewSelector;
    }

    @GetMapping(UserRouteRegistry.SIGN_UP)
    public ModelAndView signUp() {
        ModelAndView modelAndView = new ModelAndView(viewSelector.selectView(UserRouteRegistry.SIGN_UP));
        modelAndView.addObject(getUser());
        return modelAndView;
    }

    @PostMapping(UserRouteRegistry.SIGN_UP)
    public ModelAndView signUp(
            @ModelAttribute("user") @Validated(User.CreateUserGroup.class) User user,
            BindingResult result,
            WebRequest request,
            Errors errors
    ) {
//    public String registration(@ModelAttribute("user") @Validated(User.CreateUserGroup.class) User user) {
        try {
            userService.createNewUserAccount(user);
        } catch(EmailNotUniqueException e) {
            result.rejectValue(
                    "email",
                    "validator.email.exists",
                    new String[]{ user.getEmail() },
                    "OOPS");
        }
        return new ModelAndView(viewSelector.selectView(UserRouteRegistry.SIGN_UP));
    }

}
