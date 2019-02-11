package ua.tucha.passpass.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.util.RouteRegistry;
import ua.tucha.passpass.util.ViewSelector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class FailureController implements ErrorController {

    private final ViewSelector viewSelector;

    @Autowired
    public FailureController(ViewSelector viewSelector) {
        this.viewSelector = viewSelector;
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = Integer.valueOf(status.toString());
        String viewHandler =
                statusCode == HttpStatus.NOT_FOUND.value()
                        ?
                            RouteRegistry.FailureRouteRegistry.PAGE_NOT_FOUND
                        :
                statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()
                        ?
                            RouteRegistry.FailureRouteRegistry.INTERNAL_SERVER_ERROR
                        :
                            RouteRegistry.FailureRouteRegistry.DEFAULT
        ;
        return(viewSelector.selectView(viewHandler));
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
