package org.metropolia.minimalnotepad.dto;

import org.metropolia.minimalnotepad.model.Note;

import java.util.ArrayList;

public class SearchRequest {
    private String query;
    private ArrayList<Note> notes;
    public SearchRequest() {

    }
    public String getQuery() {
        return query;
    }
    public ArrayList<Note> getNotes() {
        return notes;
    }
}
