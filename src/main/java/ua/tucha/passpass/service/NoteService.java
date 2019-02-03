package ua.tucha.passpass.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.model.Note;
import ua.tucha.passpass.repository.NoteRepository;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class NoteService implements FactoryBean<NoteService> {

    // instance
    private static final NoteService noteService = new NoteService();

    // repository
    @Autowired
    private NoteRepository noteRepository;

    // Deny calling a constructor, as it's a singleton
    private NoteService() {}

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Bean
    @Override
    public NoteService getObject() throws Exception {
        return noteService;
    }

    @Override
    public Class<?> getObjectType() {
        return NoteService.class;
    }

    @PostConstruct
    private void onStart() {
        log.debug("A NoteService object is constructed, noteRepository is {}", noteRepository);
    }

    public Note getNote(@NotNull long noteId) {
        Optional<Note> note = noteRepository.findById(noteId);
        return note.isEmpty() ? null : note.get();
    }

}
