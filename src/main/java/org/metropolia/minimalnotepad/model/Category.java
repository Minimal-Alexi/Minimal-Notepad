package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "categoriesList")
    @JsonManagedReference("category-note")
    @JsonIgnore
    List<Note> noteList;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Note> getNoteList() {
        return noteList;
    }
    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
