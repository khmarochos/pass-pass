package ua.tucha.passpass.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FrontendMessageInterceptor implements HandlerInterceptor {

    @Autowired
    private FrontendMessageStackService frontendMessageStackService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        frontendMessageStackService.pushFrontendMessage(
                new FrontendMessage(
                        FrontendMessage.MessageType.INFO,
                        "hello",
                        "well, hello!"
                )
        );
        return true;
    }

}
