package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        if(note == null) {
            throw new ResourceDoesntExistException("This note doesn't exist");
        }
        if (user == null || note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        return note;
    }
    public void createNote(User user,Note note) {
        if(note == null)
        {
            throw new ResourceDoesntExistException("You do not have a note");
        }
        if (user == null || note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        noteRepository.save(note);
    }
    public Note updateNote(User user, long noteId, Note updatedNote) {

        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceDoesntExistException("This note doesn't exist"));


        if (existingNote.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }

        if (updatedNote.getTitle() != null) {
            existingNote.setTitle(updatedNote.getTitle());
        }
        if (updatedNote.getText() != null) {
            existingNote.setText(updatedNote.getText());
        }

        return noteRepository.save(existingNote);
    }

    public void deleteNote(User user,Note note) {
        if(note == null)
        {
            throw new ResourceDoesntExistException("You do not have a note");
        }
        if (user == null || note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        noteRepository.delete(note);
    }
}
