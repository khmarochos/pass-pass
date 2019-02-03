package ua.tucha.passpass.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tucha.passpass.model.Note;
import ua.tucha.passpass.service.NoteService;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;

    // TODO: implement mapping confguration (consider Swagger)
    @GetMapping("/note")
    public Note note(@RequestParam @NotNull long id) {
        Note note = noteService.getNote(id);
        return(note);
    }
}
