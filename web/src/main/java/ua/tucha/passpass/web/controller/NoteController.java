package ua.tucha.passpass.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.tucha.passpass.core.model.User;
import ua.tucha.passpass.core.service.NoteService;
import ua.tucha.passpass.core.service.UserService;
import ua.tucha.passpass.web.router.RouteRegistry;
import ua.tucha.passpass.web.router.ViewSelector;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(path = {
        RouteRegistry.NoteRouteRegistry.FIRST_LEVEL + "/*",
        RouteRegistry.NoteRouteRegistry.FIRST_LEVEL + "/*/*"
})
public class NoteController {

    private final ModelMapper modelMapper;
    private final NoteService noteService;
    private final UserService userService;
    private final ViewSelector viewSelector;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public NoteController(
            NoteService noteService,
            UserService userService,
            ViewSelector viewSelector,
            ApplicationEventPublisher eventPublisher
    ) {
        this.noteService = noteService;
        this.userService = userService;
        this.viewSelector = viewSelector;
        this.eventPublisher = eventPublisher;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping(RouteRegistry.NoteRouteRegistry.LIST)
    public String listNotes(
            Model model,
            Principal principal
    ) {
        User user = userService.findUserByEmail(principal.getName());
        log.debug("User {} would like to get the notes' lists", user.getEmail());
        model.addAttribute("receivedNoteList", user.getReceivedNoteList());
        model.addAttribute("sentNoteList", user.getSentNoteList());
        return viewSelector.selectViewByName(RouteRegistry.NoteRouteRegistry.LIST);
    }


    @GetMapping(RouteRegistry.NoteRouteRegistry.READ + "/{noteId}")
    public String readNote(
            @PathVariable long noteId,
            Model model,
            Principal principal
    ) {
        User user = userService.findUserByEmail(principal.getName());
        log.debug("User {} would like to read note {}", user.getEmail(), noteId);
        return viewSelector.selectViewByName(RouteRegistry.NoteRouteRegistry.READ);
    }

}
