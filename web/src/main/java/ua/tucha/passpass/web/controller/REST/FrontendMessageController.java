package ua.tucha.passpass.web.controller.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.tucha.passpass.web.model.FrontendMessage;
import ua.tucha.passpass.web.service.FrontendMessageStackService;

@RestController
@RequestMapping("/api/frontend-message")
public class FrontendMessageController {

    private FrontendMessageStackService frontendMessageStackService;

    @Autowired
    public FrontendMessageController(FrontendMessageStackService frontendMessageStackService) {
        this.frontendMessageStackService = frontendMessageStackService;
    }

    @GetMapping(value = "/pop", produces = "application/json")
    public FrontendMessage getFrontendMessageList() {
        return frontendMessageStackService.popFrontendMessage();
    }

}
