package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Note;
import java.util.ArrayList;

public class NoteFilter {
    private ArrayList<Note> notes;
    private Category category;

    // Constructors
    public NoteFilter() { }

    public NoteFilter(ArrayList<Note> notes, Category category) {
        this.notes = notes;
        this.category = category;
    }

    // Getters and setters
    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
