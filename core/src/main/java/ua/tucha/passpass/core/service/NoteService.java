package ua.tucha.passpass.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.Note;
import ua.tucha.passpass.core.repository.NoteRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class NoteService {

    // repository
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


}
