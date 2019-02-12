package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.util.RouteRegistry.FailureRouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class FailureController implements ErrorController {

    private static Map<Integer, String> errorCodeRoutes = new HashMap<Integer, String>() {
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

    private final ViewSelector viewSelector;

    @Autowired
    public FailureController(ViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = Integer.valueOf(status.toString());
        String viewHandler;
        log.debug("errorCodeRoutes = {}", errorCodeRoutes);
        viewHandler = (viewHandler = errorCodeRoutes.get(statusCode)) != null
                ? viewHandler
                : FailureRouteRegistry.DEFAULT;
        return(viewSelector.selectView(viewHandler));
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
