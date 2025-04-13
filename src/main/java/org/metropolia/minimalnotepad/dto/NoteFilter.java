package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Note;
import java.util.ArrayList;

/**
 * The type Note filter.
 */
public class NoteFilter {
    private ArrayList<Note> notes;
    private Category category;

    /**
     * Instantiates a new Note filter.
     */
// Constructors
    public NoteFilter() { }

    /**
     * Instantiates a new Note filter.
     *
     * @param notes    the notes
     * @param category the category
     */
    public NoteFilter(ArrayList<Note> notes, Category category) {
        this.notes = notes;
        this.category = category;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
// Getters and setters
    public ArrayList<Note> getNotes() {
        return notes;
    }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(Category category) {
        this.category = category;
    }
}
