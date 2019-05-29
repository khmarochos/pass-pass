package ua.tucha.passpass.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Autowired
    FrontendMessageStackService frontendMessageStackService;

    @Autowired
    MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException
    ) throws IOException, ServletException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        frontendMessageStackService.pushFrontendMessage(
                new FrontendMessage(
                        FrontendMessage.MessageType.ERROR,
                        messageSource.getMessage(
                                "web.security.CustomAuthenticationFailureHandler.onAuthenticationFailure.title",
                                null,
                                currentLocale
                        ),
                        messageSource.getMessage(
                                "web.security.CustomAuthenticationFailureHandler.onAuthenticationFailure.body",
                                null,
                                currentLocale
                        )
                )
        );
        response.sendRedirect(RouteRegistry.UserRouteRegistry.SIGN_IN);
    }
}
