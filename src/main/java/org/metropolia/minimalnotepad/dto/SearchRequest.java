package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Note;

import java.util.ArrayList;

public class SearchRequest {
    private String query;
    private ArrayList<Note> notes;
    public SearchRequest(String query, ArrayList<Note> notes) {
        this.query = query;
        this.notes = notes;
    }
    public String getQuery() {
        return query;
    }
    public ArrayList<Note> getNotes() {
        return notes;
    }
}
