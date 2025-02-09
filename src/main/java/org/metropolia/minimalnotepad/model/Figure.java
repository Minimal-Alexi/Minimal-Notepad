package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "figures")
public class Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String link;
    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    @JsonBackReference
    private Note note;
    public Figure() {}
    public void setNote(Note note) {
        this.note = note;
    }
    public Note getNote() {
        return note;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getLink() {
        return link;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
