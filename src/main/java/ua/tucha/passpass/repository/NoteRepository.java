package ua.tucha.passpass.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.model.Note;

public interface NoteRepository extends CrudRepository<Note, Long> {}
