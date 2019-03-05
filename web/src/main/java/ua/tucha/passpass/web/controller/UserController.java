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
import ua.tucha.passpass.core.model.VerificationTokenPurpose;
import ua.tucha.passpass.core.service.UserService;
import ua.tucha.passpass.core.service.VerificationTokenService;
import ua.tucha.passpass.core.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.core.service.exception.VerificationTokenExpiredException;
import ua.tucha.passpass.core.service.exception.VerificationTokenMispurposedException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotFoundException;
import ua.tucha.passpass.web.model.DTO.EmailDTO;
import ua.tucha.passpass.web.model.DTO.UserDTO;
import ua.tucha.passpass.web.model.DTO.VerificationTokenDTO;
import ua.tucha.passpass.web.router.RouteRegistry.UserRouteRegistry;
import ua.tucha.passpass.web.router.ViewSelector;

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
        model.addAttribute("action", UserRouteRegistry.SIGN_UP);
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", getUserDTO());
        }
        return viewSelector.selectViewByName(UserRouteRegistry.SIGN_UP);
    }


    @PostMapping(UserRouteRegistry.SIGN_UP)
    public String signUp(
            @ModelAttribute("userDTO") @Validated(UserDTO.CreateUserValidaionGroup.class) UserDTO userDTO,
            BindingResult result,
            WebRequest request,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.SIGN_UP);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", result);
        redirectAttributes.addFlashAttribute("userDTO", userDTO);
        model.addAttribute("action", UserRouteRegistry.SIGN_UP);

        if (!result.hasErrors()) {

            User userPrepared = new User();
            User userRegistered;
            modelMapper.map(userDTO, userPrepared);

            try {
                userRegistered = userService.createUser(userPrepared);
                eventPublisher.publishEvent(
                        new VerificationTokenNeeded(
                                userRegistered,
                                request.getLocale(),
                                request.getContextPath(),
                                VerificationTokenPurpose.Purpose.EMAIL_CONFIRMATION
                        )
                );
                nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
            } catch (EmailNotUniqueException e) {
                // Actually I don't like that this message won't be sent if the DTO validation fails,
                // will definitely implement an additional check sometimes (TODO)
                result.rejectValue(
                        "email",
                        "core.model.validator.EmailValidator.email_exists", new String[]{userDTO.getEmail()},
                        "This email address is already registered");
            }
        }

        // These objects are needed in any case
        return nextStep;
    }


    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN)
    public String confirmEmail(
            Model model
    ) {
        model.addAttribute("action", UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN)
    public String confirmEmail(
            @ModelAttribute("verificationTokenDTO") @Validated VerificationTokenDTO verificationTokenDTO,
            BindingResult verificationTokenDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verificationTokenDTO", verificationTokenDTOResult);
        redirectAttributes.addFlashAttribute("verificationTokenDTO", verificationTokenDTO);

        if (!verificationTokenDTOResult.hasErrors()) {
            try {
                userService.verifyEmailByToken(verificationTokenDTO.getVerificationToken());
                verificationTokenDTO.setTokenApplied(true);
                nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.SIGN_IN);
            } catch (VerificationTokenExpiredException e) {
                verificationTokenDTOResult.rejectValue(
                        "verificationToken",
                        "web.controller.UserController.verification_token_invalid",
                        "The verification code is no longer valid");
            } catch (VerificationTokenMispurposedException e) {
                verificationTokenDTOResult.rejectValue(
                        "verificationToken",
                        "web.controller.UserController.verification_token_not_purposed_for_email_confirmation",
                        "The verification code is not purposed for user email confirmation");
            } catch (VerificationTokenNotFoundException e) {
                verificationTokenDTOResult.rejectValue(
                        "verificationToken",
                        "web.controller.UserController.verification_token_not_found",
                        "The verification code does not exist");
            }
        }

        return nextStep;
    }


    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN)
    public String confirmEmailRetry(Model model) {
        model.addAttribute("action", UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN)
    public String confirmEmailRetry(
            @ModelAttribute("emailDTO") @Validated EmailDTO emailDTO,
            BindingResult emailDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO", emailDTOResult);
        redirectAttributes.addFlashAttribute("emailDTO", emailDTO);

        if(!emailDTOResult.hasErrors()) {
            User user = userService.findUserByEmail(emailDTO.getEmail());
            if(user == null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "This email is not registered");
            } else if(user.getVerified() != null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "The user's email is already verified");
            } else {
                eventPublisher.publishEvent(
                        new VerificationTokenNeeded(
                                user,
                                request.getLocale(),
                                request.getContextPath(),
                                VerificationTokenPurpose.Purpose.EMAIL_CONFIRMATION
                        )
                );
                nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
            }
        }

        return nextStep;

    }


    @Getter
    private class VerificationTokenNeeded extends ApplicationEvent {

        private User user;
        private Locale locale;
        private String appURL;
        private VerificationTokenPurpose.Purpose purpose;

        private VerificationTokenNeeded(User user, Locale locale, String appURL, VerificationTokenPurpose.Purpose purpose) {
            super(user);
            this.user = user;
            this.locale = locale;
            this.appURL = appURL;
            this.purpose = purpose;
        }
    }

    @Component
    private class VerificationTokenNeededListener implements ApplicationListener<VerificationTokenNeeded> {

        @Override
        public void onApplicationEvent(VerificationTokenNeeded event) {
            this.verifyEmail(event);
        }

        private void verifyEmail(VerificationTokenNeeded event) {
            if(event.getPurpose() == VerificationTokenPurpose.Purpose.EMAIL_CONFIRMATION) {
                // FIXME:
                //      do NOT address VerificationTokenService directly,
                //      keep in mind that UserController should interact with UserService only
                verificationTokenService.createAndSendVerificationTokenToConfirmEmail(
                        event.getUser(),
                        event.getAppURL(),
                        event.getLocale()
                );
            }
        }
    }

}
