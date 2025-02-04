package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
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
        List<Note> notesList =  noteRepository.getNotesByUserId(user.getId());
        return notesList;
    }
    public Note getNoteById(User user, long id) {
        Note note = noteRepository.getNoteById(id);
        if (note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        return note;
    }
    public boolean createNote(User user,Note note) {
        return false;
    }
    public boolean updateNote(User user,Note note) {
        return false;
    }
    public boolean deleteNote(User user,Note note) {
        return false;
    }
}
