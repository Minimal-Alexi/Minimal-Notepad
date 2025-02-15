package org.metropolia.minimalnotepad.model;

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
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference("category-note")
    private List<NoteCategories> noteCategories;

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
    public List<NoteCategories> getNoteCategories() {
        return noteCategories;
    }
    public void setNoteCategories(List<NoteCategories> noteCategories) {
        this.noteCategories = noteCategories;
    }
}
