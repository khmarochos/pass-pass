package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL)
    public String confirmEmail(
            Model model
    ) {
        return viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
    }

    @GetMapping(UserRouteRegistry.SIGN_UP)
    public String signUp(
//            @ModelAttribute("user") User user, BindingResult result
            Model model
    ) {
        if(!model.containsAttribute("user")) {
            model.addAttribute("user", getUser());
        }
//        log.debug("GET: Model >>> {}", model);
//        log.debug("GET: BindingResult >>> {}, {}, {}", result, result.hasErrors(), result.hasFieldErrors());
//        log.debug("GET: Errors >>> {}", result.hasErrors());
        return viewSelector.selectView(UserRouteRegistry.SIGN_UP);
    }

    @PostMapping(UserRouteRegistry.SIGN_UP)
    public String signUp(
            @ModelAttribute("user") @Validated(User.CreateUserGroup.class) User user, BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectView(UserRouteRegistry.SIGN_UP);
        if(!result.hasErrors()) {
            try {
                userService.createNewUserAccount(user);
                nextStep = "redirect:" + viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
            } catch (EmailNotUniqueException e) {
                result.rejectValue(
                        "email",
                        "validator.email.exists", new String[]{user.getEmail()},
                        "This email address is already registered");
            }
        }
        // These objects are needed in any case
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
        redirectAttributes.addFlashAttribute("user", user);
        return nextStep;
    }

}
