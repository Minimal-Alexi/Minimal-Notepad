package org.metropolia.minimalnotepad.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NoteCategoriesId implements Serializable {
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "note_id")
    private Long noteId;

    public NoteCategoriesId() {

    }
    public NoteCategoriesId(Long categoryId, Long noteId) {
        this.categoryId = categoryId;
        this.noteId = noteId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public Long getNoteId() {
        return noteId;
    }
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteCategoriesId that = (NoteCategoriesId) o;
        return Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(noteId, that.noteId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(categoryId, noteId);
    }

}
