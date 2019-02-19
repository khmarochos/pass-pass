package ua.tucha.passpass.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.tucha.passpass.controller.dto.UserDTO;
import ua.tucha.passpass.model.User;
import ua.tucha.passpass.service.UserService;
import ua.tucha.passpass.service.VerificationTokenService;
import ua.tucha.passpass.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.util.RouteRegistry.UserRouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(UserRouteRegistry.FIRST_LEVEL + "/*")
public class UserController {

    @ModelAttribute("userDTO")
    public UserDTO getUserDTO() {
        return new UserDTO();
    }

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ViewSelector viewSelector;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;



    @Autowired
    public UserController(
            UserService userService,
            VerificationTokenService verificationTokenService,
            ViewSelector viewSelector,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.viewSelector = viewSelector;
        this.eventPublisher = eventPublisher;

        this.modelMapper = new ModelMapper();
    }



    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL)
    public String confirmEmail(
            Model model
    ) {
        return viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
    }



    @GetMapping(UserRouteRegistry.SIGN_UP)
    public String signUp(
            Model model
    ) {
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", getUserDTO());
        }
        return viewSelector.selectView(UserRouteRegistry.SIGN_UP);
    }



    @PostMapping(UserRouteRegistry.SIGN_UP)
    public String signUp(
            @ModelAttribute("userDTO") @Validated(UserDTO.CreateUserGroup.class) UserDTO userDTO,
            BindingResult result,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {

        String nextStep = "redirect:" + viewSelector.selectView(UserRouteRegistry.SIGN_UP);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", result);
        redirectAttributes.addFlashAttribute("userDTO", userDTO);

        if (!result.hasErrors()) {

            User userPrepared = new User();
            User userRegistered;
            modelMapper.map(userDTO, userPrepared);

            try {
                userRegistered = userService.createUser(userPrepared);
                nextStep = "redirect:" + viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
            } catch (EmailNotUniqueException e) {
                result.rejectValue(
                        "email",
                        "validator.email.exists", new String[]{userDTO.getEmail()},
                        "This email address is already registered");
                return nextStep;
            }

            try {
                eventPublisher.publishEvent(
                        new SignUpCompletedEvent(
                                userRegistered,
                                request.getLocale(),
                                request.getContextPath()
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // These objects are needed in any case
        return nextStep;
    }



    @Getter
    private class SignUpCompletedEvent extends ApplicationEvent {

        private String appURL;
        private Locale locale;
        private User user;

        private SignUpCompletedEvent(User user, Locale locale, String appURL) {
            super(user);
            this.user = user;
            this.locale = locale;
            this.appURL = appURL;
        }
    }



    @Component
    private class SignUpCompletedListener implements ApplicationListener<SignUpCompletedEvent> {

        @Override
        public void onApplicationEvent(SignUpCompletedEvent event) {
            this.verifyEmail(event);
        }

        private void verifyEmail(SignUpCompletedEvent event) {
            User user = event.getUser();
            Locale locale = event.getLocale();
            verificationTokenService.sendVerificationToken(user, null, locale);
        }
    }

}
