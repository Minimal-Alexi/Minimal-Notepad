package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Note;

import java.util.ArrayList;

/**
 * The type Search request.
 */
public class SearchRequest {
    private String query;
    private ArrayList<Note> notes;

    /**
     * Instantiates a new Search request.
     *
     * @param query the query
     * @param notes the notes
     */
    public SearchRequest(String query, ArrayList<Note> notes) {
        this.query = query;
        this.notes = notes;
    }

    /**
     * Gets query.
     *
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }
}
