package ua.tucha.passpass.core.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.core.model.Note;

public interface NoteRepository extends CrudRepository<Note, Long> {}
