package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.Note;
import ua.tucha.passpass.repository.NoteRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class NoteService {

    // repository
    @Autowired
    private NoteRepository noteRepository;

    public Note getNote(@NotNull long noteId) {
        Optional<Note> note = noteRepository.findById(noteId);
        return note.isEmpty() ? null : note.get();
    }

}
