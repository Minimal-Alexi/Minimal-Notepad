package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Note repository.
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    /**
     * Gets notes by user id.
     *
     * @param userId the user id
     * @return the notes by user id
     */
    List<Note> getNotesByUserId(long userId);

    /**
     * Gets note by id.
     *
     * @param id the id
     * @return the note by id
     */
    Note getNoteById(long id);
}
