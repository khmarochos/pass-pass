package ua.tucha.passpass.web.controller;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.web.util.RouteRegistry.FailureRouteRegistry;
import ua.tucha.passpass.web.util.ViewSelector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



@Slf4j
@Controller
public class FailureController implements ErrorController {

    @Data
    private class FailureMessageBundle {
        private final String title;
        private final String code;
        private final String name;
        private final String description;
    }

    private static final Map<Integer, String> failureCodeRoute = new HashMap<Integer, String>() {
        {
            put(
                              HttpStatus.NOT_FOUND.value(),
                    FailureRouteRegistry.NOT_FOUND
            );
            put(
                              HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    FailureRouteRegistry.INTERNAL_SERVER_ERROR
            );
        }
    };

    private final MessageSource messageSource;
    private final ViewSelector viewSelector;

    @Autowired
    public FailureController(ViewSelector viewSelector, MessageSource messageSource) {
        this.viewSelector = viewSelector;
        this.messageSource = messageSource;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = Integer.valueOf(status.toString());
        FailureMessageBundle failureMessages = getFailureMessages(statusCode);
        model.addAttribute("failure", failureMessages);
        return(getFailureViewName(statusCode));
    }

    // This method fills a message bundle with localized messages
    private FailureMessageBundle getFailureMessages(@NonNull Integer statusCode) {
        Locale locale = Locale.getDefault();
        String statusCodeString = statusCode.toString();
        String messageLabelPrefix = "failure." + statusCodeString;
        String failureTitle = messageSource.getMessage(
                messageLabelPrefix + ".title", null, statusCodeString, locale);
        String failureCode = messageSource.getMessage(
                messageLabelPrefix + ".code", null, statusCodeString, locale);
        String failureName = messageSource.getMessage(
                messageLabelPrefix + ".name", null, "UNNAMED FAILURE", locale);
        String failureDescription = messageSource.getMessage(
                messageLabelPrefix + ".description", null, "NO FAILURE DESCRIPTION", locale);
        return new FailureMessageBundle(failureTitle, failureCode, failureName, failureDescription);
    }

    // This method selects a view name
    private String getFailureViewName(Integer statusCode) {
        String viewHandler;
        viewHandler = (viewHandler = failureCodeRoute.get(statusCode)) != null
                ? viewHandler
                : FailureRouteRegistry.DEFAULT;
        return(viewSelector.selectView(viewHandler));
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
