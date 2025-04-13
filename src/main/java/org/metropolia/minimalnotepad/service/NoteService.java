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

/**
 * The type Note service.
 */
@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final GroupService groupService;
    private final SearchUtils searchUtils;

    /**
     * Instantiates a new Note service.
     *
     * @param noteRepository the note repository
     * @param searchUtils    the search utils
     * @param groupService   the group service
     */
    public NoteService(NoteRepository noteRepository, SearchUtils searchUtils, GroupService groupService) {
        this.noteRepository = noteRepository;
        this.searchUtils = searchUtils;
        this.groupService = groupService;
    }

    /**
     * Gets note lists by user.
     *
     * @param user the user
     * @return the note lists by user
     */
    public List<Note> getNoteListsByUser(User user) {
        List<Note> notesList = noteRepository.getNotesByUserId(user.getId());
        for (Note note : notesList) {
            if (note.getCategoriesList() != null && !note.getCategoriesList().isEmpty()) {
                note.categoryLocalization(user.getLanguage());
            }
        }
        return notesList;
    }

    /**
     * Gets note by id.
     *
     * @param user the user
     * @param id   the id
     * @return the note by id
     */
    public Note getNoteById(User user, long id) {
        Note note = noteRepository.getNoteById(id);
        if (note == null) {
            throw new ResourceDoesntExistException("This note doesn't exist");
        }
        if (user == null) {
            throw new UserDoesntOwnResourceException("This user doesn't exist");
        }

        if (note.getUser().getId() == user.getId()) {
            if (note.getCategoriesList() != null && !note.getCategoriesList().isEmpty()) {
                note.categoryLocalization(user.getLanguage());
            }
            return note;
        }

        List<Group> groups = groupService.getUserGroups(user);
        Group group = note.getGroup();

        if (group != null && groups.contains(group)) {
            return note;
        }
        throw new UserDoesntOwnResourceException("You do not have access to this note.");
    }

    /**
     * Gets notes from groups.
     *
     * @param groups the groups
     * @param user   the user
     * @return the notes from groups
     */
    public List<Note> getNotesFromGroups(List<Group> groups, User user) {
        List<Note> notes = new ArrayList<>();

        for (Group group : groups) {
            if (group.getNotes() != null) {
                notes.addAll(group.getNotes());
            }
        }

        for (Note note : notes) {
            note.categoryLocalization(user.getLanguage());
        }

        return notes;
    }

    /**
     * Create note.
     *
     * @param user the user
     * @param note the note
     */
    public void createNote(User user, Note note) {
        if (note == null) {
            throw new ResourceDoesntExistException("You do not have a note");
        }
        if (user == null || note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        noteRepository.save(note);
    }

    /**
     * Update note note.
     *
     * @param user        the user
     * @param noteId      the note id
     * @param updatedNote the updated note
     * @return the note
     */
    public Note updateNote(User user, long noteId, Note updatedNote) {
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceDoesntExistException("This note doesn't exist"));

        if (existingNote.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        updatedNote.categoryLocalization(user.getLanguage());
        updatedNote.setId(noteId);
        updatedNote.setUser(user);
        updatedNote.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        updatedNote.setCreatedAt(existingNote.getCreatedAt());
        return noteRepository.save(updatedNote);
    }

    /**
     * Delete note.
     *
     * @param user the user
     * @param note the note
     */
    public void deleteNote(User user, Note note) {
        if (note == null) {
            throw new ResourceDoesntExistException("You do not have a note");
        }
        if (user == null || note.getUser().getId() != user.getId()) {
            throw new UserDoesntOwnResourceException("You do not own this note.");
        }
        noteRepository.delete(note);
    }

    /**
     * Filter notes array list.
     *
     * @param unfilteredNotes the unfiltered notes
     * @param filterCategory  the filter category
     * @return the array list
     */
    public ArrayList<Note> filterNotes(ArrayList<Note> unfilteredNotes, Category filterCategory) {
        ArrayList<Note> filteredNotes = new ArrayList<>();
        if (filterCategory != null) {
            for (Note note : unfilteredNotes) {
                ArrayList<Category> categoryList = (ArrayList<Category>) note.getCategoriesList();
                if (categoryList.stream().anyMatch(category -> category.getId() == filterCategory.getId())) {
                    filteredNotes.add(note);
                }
            }
        } else {
            for (Note note : unfilteredNotes) {
                if (note.getCategoriesList().isEmpty()) {
                    filteredNotes.add(note);
                }
            }
        }
        return filteredNotes;
    }

    /**
     * Find notes array list.
     *
     * @param unfilteredNotes the unfiltered notes
     * @param searchTerm      the search term
     * @return the array list
     */
    public ArrayList<Note> findNotes(ArrayList<Note> unfilteredNotes, String searchTerm) {
        ArrayList<Note> foundNotes = new ArrayList<>();
        ArrayList<String> titleList = unfilteredNotes.stream().map(Note::getTitle)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer> foundNotesIndexes = searchUtils.searchText(titleList, searchTerm);
        for (int i = 0; i < foundNotesIndexes.size(); i++) {
            foundNotes.add(unfilteredNotes.get(foundNotesIndexes.get(i)));
        }
        return foundNotes;
    }
}
