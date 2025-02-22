package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Note;
import java.util.List;

public class NoteFilter {
    private List<Note> notes;
    private String category;

    // Constructors
    public NoteFilter() {}

    public NoteFilter(List<Note> notes, String category) {
        this.notes = notes;
        this.category = category;
    }

    // Getters and setters
    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}