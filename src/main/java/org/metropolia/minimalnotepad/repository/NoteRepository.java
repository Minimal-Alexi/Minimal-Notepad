package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;

import java.util.List;

public interface NoteRepository {
    List<Note> getNotesByUser(User user);
    Note getNoteById(long id);
}
