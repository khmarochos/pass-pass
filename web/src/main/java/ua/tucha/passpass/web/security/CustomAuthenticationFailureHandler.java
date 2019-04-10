package ua.tucha.passpass.web.security;

import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class CustomAuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Autowired
    FrontendMessageStackService frontendMessageStackService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        frontendMessageStackService.pushFrontendMessage(
                new FrontendMessage(
                        FrontendMessage.MessageType.ERROR,
                        "Oops!",
                        "Authentication failure :-("
                )
        );
        response.sendRedirect(RouteRegistry.UserRouteRegistry.SIGN_IN);
    }
}
