package ua.tucha.passpass.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.model.FrontendMessageStack;

import javax.servlet.http.HttpSession;

@Service
public class FrontendMessageStackService {

    private static final String FRONTEND_MESSAGE_CONTAINER_ATTRIBUTE_NAME =
            "frontendMessageContainer";


    private FrontendMessageStack initializeFrontendMessageStack() {

        HttpSession httpSession = (
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()
        )
                .getRequest()
                .getSession(true);

        FrontendMessageStack frontendMessageStack =
                (FrontendMessageStack) httpSession.getAttribute(FRONTEND_MESSAGE_CONTAINER_ATTRIBUTE_NAME);

        if (frontendMessageStack == null) {
            frontendMessageStack = new FrontendMessageStack();
            httpSession.setAttribute(FRONTEND_MESSAGE_CONTAINER_ATTRIBUTE_NAME, frontendMessageStack);
        }

        return frontendMessageStack;

    }


    public void pushFrontendMessage(FrontendMessage frontendMessage) {

        FrontendMessageStack frontendMessageStack = initializeFrontendMessageStack();

        frontendMessageStack.pushFrontendMessage(frontendMessage);

    }


    public FrontendMessage popFrontendMessage() {

        FrontendMessageStack frontendMessageStack = initializeFrontendMessageStack();

        return frontendMessageStack.popFrontendMessage();

    }


    public int hasFrontendMessage() {

        FrontendMessageStack frontendMessageStack = initializeFrontendMessageStack();

        return frontendMessageStack.hasFrontendMessage();

    }

}
