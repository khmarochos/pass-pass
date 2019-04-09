package ua.tucha.passpass.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Autowired
    FrontendMessageStackService frontendMessageStackService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        frontendMessageStackService.pushFrontendMessage(
                new FrontendMessage(
                        FrontendMessage.MessageType.SUCCESS,
                        "Welcome!",
                        "Nice to see you, ..."
                )
        );
        response.sendRedirect(RouteRegistry.HOME);
    }
}
