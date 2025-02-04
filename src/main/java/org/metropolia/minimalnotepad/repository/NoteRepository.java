package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> getNotesByUserId(long userId);
    Note getNoteByIdAndUserId(long id, long userId);
}
