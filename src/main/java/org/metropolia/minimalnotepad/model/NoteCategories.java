package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "note_categories")
public class NoteCategories {
    @EmbeddedId
    private NoteCategoriesId id = new NoteCategoriesId();
    @ManyToOne
    @MapsId("note_id")
    @JoinColumn(name = "note_id")
    @JsonBackReference("note-tags")
    private Note note;
    @ManyToOne
    @MapsId("category_id")
    @JoinColumn(name = "category_id")
    @JsonBackReference("category-note")
    private Category category;

    public NoteCategories() {

    }
    public Note getNote() {
        return note;
    }
    public void setNote(Note note) {
        this.note = note;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

}
