package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.tucha.passpass.model.Note;
import ua.tucha.passpass.service.NoteService;
import ua.tucha.passpass.util.RouteRegistry;

@Slf4j
@RestController
@RequestMapping(RouteRegistry.NoteRouteRegistry.FIRST_LEVEL + "/*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{id}")
    public Note note(@PathVariable(value="id") int id) {
        Note note = noteService.getNote(id);
        return(note);
    }
}
