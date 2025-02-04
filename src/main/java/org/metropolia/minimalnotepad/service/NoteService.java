package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;

import java.util.List;

public class NoteService {
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    public NoteService(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }
    public List<Note> getNoteListsByUser(User user) {
        return null;
    }
    public Note getNoteById(User user, long id) {
        return null;
    }
    public Note createNote(User user,Note note) {
        return null;
    }
    public Note updateNote(User user,Note note) {
        return null;
    }
    public void deleteNote(User user,Note note) {

    }
}
