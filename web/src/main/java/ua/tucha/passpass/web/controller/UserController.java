package ua.tucha.passpass.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import ua.tucha.passpass.core.constant.VerificationTokenPurpose;
import ua.tucha.passpass.core.service.UserService;
import ua.tucha.passpass.core.service.exception.EmailNotUniqueException;
import ua.tucha.passpass.core.service.exception.VerificationTokenExpiredException;
import ua.tucha.passpass.core.service.exception.VerificationTokenMispurposedException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotAppliedException;
import ua.tucha.passpass.core.service.exception.VerificationTokenNotFoundException;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.model.formdata.EmailDTO;
import ua.tucha.passpass.web.model.formdata.UserDTO;
import ua.tucha.passpass.web.model.formdata.VerificationTokenDTO;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.router.RouteRegistry.UserRouteRegistry;
import ua.tucha.passpass.web.router.ViewSelector;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.Principal;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

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

    @ModelAttribute("emailDTO")
    public EmailDTO getEmailDTO() {
        return new EmailDTO();
    }

    @ModelAttribute("verificationTokenDTO")
    public VerificationTokenDTO getVerificationTokenDTO() {
        return new VerificationTokenDTO();
    }

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FrontendMessageStackService frontendMessageStackService;
    private final ViewSelector viewSelector;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(
            UserService userService,
            FrontendMessageStackService frontendMessageStackService,
            ViewSelector viewSelector,
            ApplicationEventPublisher eventPublisher,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.frontendMessageStackService = frontendMessageStackService;
        this.viewSelector = viewSelector;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.modelMapper = new ModelMapper();
    }

    //
    // AUTHORIZED USERS' PROCEDURES
    //

    // SIGN OUT PROCEDURE

    @GetMapping(UserRouteRegistry.SIGN_OUT)
    public String signOut(Model model, Principal principal) {
        if(principal != null)
            log.debug("User {} is considering signing out", principal.getName());
        model.addAttribute("action", UserRouteRegistry.SIGN_OUT);
        return viewSelector.selectViewByName(UserRouteRegistry.SIGN_OUT);
    }


    //
    // UNAUTHORIZED USERS' PROCEDURES
    //

    // SIGN IN PROCEDURE

    @GetMapping(UserRouteRegistry.SIGN_IN)
    public String signIn(Model model, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        Exception springSecurityException = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (springSecurityException != null) {
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            model.addAttribute("springSecurityExceptionMessage", springSecurityException.getMessage());
        }
        model.addAttribute("action", UserRouteRegistry.SIGN_IN);
        return viewSelector.selectViewByName(UserRouteRegistry.SIGN_IN);
    }


    // PASSWORD RECOVERY PROCEDURE

    @GetMapping(UserRouteRegistry.RESET_PASSWORD_ORDER_TOKEN)
    public String resetPasswordOrderToken(Model model) {
        model.addAttribute("action", UserRouteRegistry.RESET_PASSWORD_ORDER_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.RESET_PASSWORD_ORDER_TOKEN);
    }

    @PostMapping(UserRouteRegistry.RESET_PASSWORD_ORDER_TOKEN)
    public String resetPasswordOrderToken(
            @ModelAttribute("emailDTO") @Validated EmailDTO emailDTO,
            BindingResult emailDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.RESET_PASSWORD_ORDER_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO", emailDTOResult);
        redirectAttributes.addFlashAttribute("emailDTO", emailDTO);

        if (!emailDTOResult.hasErrors()) {
            User user = userService.findUserByEmail(emailDTO.getEmail());
            if (user == null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "This email is not registered");
            } else if (user.getDisabled() != null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "The owner's account is disabled");
            } else if (user.getVerified() == null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "The owner's email is not verified yet");
            } else {
                eventPublisher.publishEvent(
                        userService.verificationTokenNeeded(
                                user,
                                request.getLocale(),
                                request.getContextPath(),
                                VerificationTokenPurpose.Purpose.PASSWORD_RECOVERY
                        )
                );
                nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN);
            }
        }

        return nextStep;

    }

    @GetMapping(UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN)
    public String resetPasswordApplyToken(Model model) {
        model.addAttribute("action", UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN);
    }

    @PostMapping(UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN)
    public String resetPasswordApplyToken(
            @ModelAttribute("verificationTokenDTO") @Validated VerificationTokenDTO verificationTokenDTO,
            BindingResult verificationTokenDTOResult,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.RESET_PASSWORD_APPLY_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verificationTokenDTO", verificationTokenDTOResult);
        redirectAttributes.addFlashAttribute("verificationTokenDTO", verificationTokenDTO);

        if (!verificationTokenDTOResult.hasErrors()) {
            if(applyVerificationToken(
                    verificationTokenDTO.getVerificationToken(),
                    VerificationTokenPurpose.Purpose.PASSWORD_RECOVERY,
                    verificationTokenDTOResult,
                    httpServletRequest.getSession(true)
            )) {
                nextStep = "redirect:" + viewSelector.selectPathByName(RouteRegistry.HOME);
                frontendMessageStackService.pushFrontendMessage(
                        new FrontendMessage(
                                FrontendMessage.MessageType.INFO,
                                "Welcome!",
                                "Temporary access granted♦"
                                // TODO: get these messages from `message.properties`
                        )
                );
            }
        }

        return nextStep;


    }


    //
    // NEW USERS' PROCEDURES
    //

    // SIGN UP PROCEDURE

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
                        userService.verificationTokenNeeded(
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


    // EMAIL CONFIRMATION PROCEDURE (TOKEN RESEND)

    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN)
    public String confirmEmailOrderToken(Model model) {
        model.addAttribute("action", UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN)
    public String confirmEmailOrderToken(
            @ModelAttribute("emailDTO") @Validated EmailDTO emailDTO,
            BindingResult emailDTOResult,
            WebRequest request,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_ORDER_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO", emailDTOResult);
        redirectAttributes.addFlashAttribute("emailDTO", emailDTO);

        if (!emailDTOResult.hasErrors()) {
            User user = userService.findUserByEmail(emailDTO.getEmail());
            if (user == null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "This email is not registered");
            } else if (user.getDisabled() != null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "The owner's account is disabled");
            } else if (user.getVerified() != null) {
                emailDTOResult.rejectValue(
                        "email",
                        "FIXME",
                        "The owner's email is already verified");
            } else {
                eventPublisher.publishEvent(
                        userService.verificationTokenNeeded(
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


    // EMAIL CONFIRMATION PROCEDURE (FINAL)

    @GetMapping(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN)
    public String confirmEmailApplyToken(
            Model model
    ) {
        model.addAttribute("action", UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
        return viewSelector.selectViewByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
    }


    @PostMapping(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN)
    public String confirmEmailApplyToken(
            @ModelAttribute("verificationTokenDTO") @Validated VerificationTokenDTO verificationTokenDTO,
            BindingResult verificationTokenDTOResult,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {
        String nextStep = "redirect:" + viewSelector.selectPathByName(UserRouteRegistry.CONFIRM_EMAIL_APPLY_TOKEN);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.verificationTokenDTO", verificationTokenDTOResult);
        redirectAttributes.addFlashAttribute("verificationTokenDTO", verificationTokenDTO);

        if (!verificationTokenDTOResult.hasErrors()) {
             if(applyVerificationToken(
                    verificationTokenDTO.getVerificationToken(),
                    VerificationTokenPurpose.Purpose.EMAIL_CONFIRMATION,
                    verificationTokenDTOResult,
                    null
            )) {
                nextStep = "redirect:" + viewSelector.selectPathByName(RouteRegistry.HOME);
             }
        }

        return nextStep;
    }

    private boolean applyVerificationToken(
            String token,
            VerificationTokenPurpose.Purpose verificationTokenPurpose,
            BindingResult verificationTokenDTOResult,
            HttpSession httpSession
    )  {
        User user = null;
        try {
            switch (verificationTokenPurpose) {
                case PASSWORD_RECOVERY:
                    log.debug("Password recovery initiated with token {}", token);
                    user = userService.authenticateByToken(token);
                    break;
                case EMAIL_CONFIRMATION:
                    log.debug("Email confirmation initiated with token {}", token);
                    user = userService.verifyEmailByToken(token);
                    break;
            }
            if(user == null)
                throw new VerificationTokenNotAppliedException();
            log.debug("Token {} belongs to user {}", token, user.getEmail());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getGrantedAuthorityList());
            log.debug("A new UserNamePasswordAuthenticationToken object created: {}", usernamePasswordAuthenticationToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            log.debug("A SecurityContext object found: {}", securityContext);
            securityContext.setAuthentication(usernamePasswordAuthenticationToken);
            httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
            log.debug("Granted temporary access to {}", user.getEmail());
            return(true);
        } catch (VerificationTokenExpiredException e) {
            verificationTokenDTOResult.rejectValue(
                    "verificationToken",
                    "web.controller.UserController.verification_token_invalid",
                    "The verification code is no longer valid");
        } catch (VerificationTokenMispurposedException e) {
            verificationTokenDTOResult.rejectValue(
                    "verificationToken",
                    "web.controller.UserController.verification_token_not_purposed_for_email_confirmation",
                    "The verification code is not purposed for owner email confirmation");
        } catch (VerificationTokenNotFoundException e) {
            verificationTokenDTOResult.rejectValue(
                    "verificationToken",
                    "web.controller.UserController.verification_token_not_found",
                    "The verification code does not exist");
        } catch (VerificationTokenNotAppliedException e) {
            verificationTokenDTOResult.rejectValue(
                    "verificationToken",
                    "web.controller.UserController.verification_token_not_applied",
                    "The verification code didn't work");
        } catch (Exception e) {
            e.printStackTrace();
            verificationTokenDTOResult.rejectValue(
                    "verificationToken",
                    "web.controller.UserController.verification_token_not_applied",
                    "Something went wrong");
        }
        return false;
    }

}
