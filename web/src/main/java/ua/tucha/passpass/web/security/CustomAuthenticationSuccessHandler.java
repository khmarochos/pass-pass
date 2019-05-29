package ua.tucha.passpass.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    FrontendMessageStackService frontendMessageStackService;

    @Autowired
    MessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        frontendMessageStackService.pushFrontendMessage(
                new FrontendMessage(
                        FrontendMessage.MessageType.SUCCESS,
                        messageSource.getMessage(
                                "web.security.CustomAuthenticationSuccessHandler.onAuthenticationSuccess.title",
                                null,
                                currentLocale
                        ),
                        messageSource.getMessage(
                                "web.security.CustomAuthenticationSuccessHandler.onAuthenticationSuccess.body",
                                new String[] { ((UserDetails) authentication.getPrincipal()).getUsername() },
                                currentLocale
                        )
                )
        );
        String urlToRedirect = RouteRegistry.HOME;
        HttpSession httpSession = request.getSession();
        try {
            urlToRedirect =
                    ((SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST"))
                            .getRedirectUrl();
        } catch (NullPointerException e) {
            //
        }
        log.debug("Authentication completed, redirecting to {}", urlToRedirect);
        response.sendRedirect(urlToRedirect);
    }

}
