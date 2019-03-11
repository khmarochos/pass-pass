package ua.tucha.passpass.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.core.service.NoteService;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.router.ViewSelector;

@Slf4j
@Controller
@RequestMapping(path = {
        RouteRegistry.NoteRouteRegistry.FIRST_LEVEL + "/*",
        RouteRegistry.NoteRouteRegistry.FIRST_LEVEL + "/*/*"
})
public class NoteController {

    private final NoteService noteService;
    private final ViewSelector viewSelector;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    @Autowired
    public NoteController(
            NoteService noteService,
            ViewSelector viewSelector,
            ApplicationEventPublisher eventPublisher
    ) {
        this.noteService = noteService;
        this.viewSelector = viewSelector;
        this.eventPublisher = eventPublisher;

        this.modelMapper = new ModelMapper();
    }

    @GetMapping(RouteRegistry.NoteRouteRegistry.FIRST_LEVEL)
    public String listNotes(Model model) {
        model.addAttribute("action", RouteRegistry.NoteRouteRegistry.FIRST_LEVEL);
        return viewSelector.selectViewByName(RouteRegistry.NoteRouteRegistry.FIRST_LEVEL);
    }
}
