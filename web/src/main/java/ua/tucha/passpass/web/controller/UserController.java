package ua.tucha.passpass.web.controller;

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
import ua.tucha.passpass.core.model.User;
import ua.tucha.passpass.core.service.UserService;
import ua.tucha.passpass.core.service.VerificationTokenService;
import ua.tucha.passpass.core.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.core.service.exception.VerificationTokenExpiredException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotFoundException;
import ua.tucha.passpass.web.model.DTO.EmailDTO;
import ua.tucha.passpass.web.model.DTO.UserDTO;
import ua.tucha.passpass.web.model.DTO.VerificationTokenDTO;
import ua.tucha.passpass.web.util.RouteRegistry.UserRouteRegistry;
import ua.tucha.passpass.web.util.ViewSelector;

import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(path = {
        UserRouteRegistry.FIRST_LEVEL + "/*",
        UserRouteRegistry.FIRST_LEVEL + "/*/*"
})
public class UserController {

    @ModelAttribute("userDTO")
    public UserDTO getUserDTO() {
        return new UserDTO();
    }

    @ModelAttribute("verificationTokenDTO")
    public VerificationTokenDTO getVerificationTokenDTO() {
        return new VerificationTokenDTO();
    }

    @ModelAttribute("emailDTO")
    public EmailDTO getEmailDTO() {
        return new EmailDTO();
    }

    private final ViewSelector viewSelector;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    public static final String ACTION_APPLY_TOKEN = "apply-token";
    public static final String ACTION_ORDER_TOKEN = "order-token";


    @Autowired
    public UserController(
            ViewSelector viewSelector,
            UserService userService,
            VerificationTokenService verificationTokenService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.viewSelector = viewSelector;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.eventPublisher = eventPublisher;

        this.modelMapper = new ModelMapper();
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
            @ModelAttribute("userDTO") @Validated(UserDTO.CreateUserValidaionGroup.class) UserDTO userDTO,
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
                // Actually I don't like that this message won't be sent if the DTO validation fails,
                // will definitely implement an additional check sometimes (TODO)
                result.rejectValue(
                        "email",
                        "core.model.validator.EmailValidator.email_exists", new String[]{userDTO.getEmail()},
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


    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL)
    public String confirmEmail(
            Model model
    ) {
        model.addAttribute("action", ACTION_APPLY_TOKEN);
        return viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL)
    public String confirmEmail(
            @ModelAttribute("verificationTokenDTO") @Validated VerificationTokenDTO verificationTokenDTO,
            BindingResult verificationTokenDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verificationTokenDTO", verificationTokenDTOResult);
        redirectAttributes.addFlashAttribute("verificationTokenDTO", verificationTokenDTO);

        if (!verificationTokenDTOResult.hasErrors()) {
            try {
                userService.verifyEmailByToken(verificationTokenDTO.getVerificationToken());
                verificationTokenDTO.setTokenApplied(true);
            } catch (VerificationTokenExpiredException e) {
                // TODO: add the corresponding entry to message.properties
                verificationTokenDTOResult.rejectValue(
                        "verificationToken",
                        "web.controller.UserController.verification_token_invalid",
                        "The verification code is no longer valid");
            } catch (VerificationTokenNotFoundException e) {
                // TODO: add the corresponding entry to message.properties
                verificationTokenDTOResult.rejectValue(
                        "verificationToken",
                        "web.controller.UserController.verification_token_not_found",
                        "The verification code does not exist");
            }
        }

        return "redirect:" + viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
    }


    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL + '/' + ACTION_ORDER_TOKEN)
    public String confirmEmailRetry(Model model) {
        model.addAttribute("action", ACTION_ORDER_TOKEN);
        return viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL + '/' + ACTION_ORDER_TOKEN)
    public String confirmEmailRetry(
            @ModelAttribute("emailDTO") @Validated EmailDTO emailDTO,
            BindingResult emailDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO", emailDTOResult);
        redirectAttributes.addFlashAttribute("emailDTO", emailDTO);

//        if(!emailDTOResult.hasErrors()) {
//
//        }

//        log.debug("emailDTO = {}, emailDTOResult = {}", emailDTO, emailDTOResult);

        return "redirect:" + viewSelector.selectView(UserRouteRegistry.CONFIRM_EMAIL) + '/' + ACTION_ORDER_TOKEN;

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
            String appURL = event.getAppURL();
            Locale locale = event.getLocale();
            verificationTokenService.sendVerificationTokenToConfirmEmail(user, appURL, locale);
        }
    }

}
