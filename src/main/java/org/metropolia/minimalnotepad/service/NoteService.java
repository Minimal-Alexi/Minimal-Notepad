package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.utils.SearchUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final GroupService groupService;
    private SearchUtils searchUtils;
    public NoteService(NoteRepository noteRepository, SearchUtils searchUtils, GroupService groupService) {
        this.noteRepository = noteRepository;
        this.searchUtils = searchUtils;
        this.groupService = groupService;
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
        if (user == null) {
            throw new UserDoesntOwnResourceException("This user doesn't exist");
        }

        if (note.getUser().getId() == user.getId()) {
            return note;
        }

        List<Group> groups = groupService.getUserGroups(user);
        Group group = note.getGroup();

        if (group != null && groups.contains(group)) {
            return note;
        }
        throw new UserDoesntOwnResourceException("You do not have access to this note.");
    }
    public List<Note> getNotesFromGroups(List<Group> groups) {
        List<Note> notes = new ArrayList<>();

        for (Group group : groups) {
            if (group.getNotes() != null) {
                notes.addAll(group.getNotes());
            }
        }

        return notes;
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
        updatedNote.setId(noteId);
        updatedNote.setUser(user);
        updatedNote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        updatedNote.setCreatedAt(existingNote.getCreatedAt());
        return noteRepository.save(updatedNote);
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
    public ArrayList<Note> filterNotes(ArrayList<Note> unfilteredNotes, Category filterCategory){
        ArrayList<Note> filteredNotes = new ArrayList<>();
        if(filterCategory != null) {
            for(Note note : unfilteredNotes){
                ArrayList<Category> categoryList = (ArrayList<Category>) note.getCategoriesList();
                if(categoryList.stream().anyMatch(category -> category.getId() == filterCategory.getId()))
                {
                    filteredNotes.add(note);
                }
            }
        }
        else
        {
            for(Note note : unfilteredNotes){
                if(note.getCategoriesList().isEmpty())
                {
                    filteredNotes.add(note);
                }
            }
        }
        return filteredNotes;
    }
    public ArrayList<Note> findNotes(ArrayList<Note> unfilteredNotes, String searchTerm){
        ArrayList<Note> foundNotes = new ArrayList<>();
        ArrayList<String> titleList = unfilteredNotes.stream().map(Note::getTitle)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> foundNotesIndexes = searchUtils.searchText(titleList, searchTerm);
        for(int i = 0; i < foundNotesIndexes.size(); i++){
            foundNotes.add(unfilteredNotes.get(foundNotesIndexes.get(i)));
        }
        return foundNotes;
    }
}
