package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The type Figure.
 */
@Entity
@Table(name = "figures")
public class Figure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String link;
    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    @JsonBackReference("figure-reference")
    private Note note;

    /**
     * Instantiates a new Figure.
     */
    public Figure() { }

    /**
     * Sets note.
     *
     * @param note the note
     */
    public void setNote(Note note) {
        this.note = note;
    }

    /**
     * Gets note.
     *
     * @return the note
     */
    public Note getNote() {
        return note;
    }

    /**
     * Sets link.
     *
     * @param link the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }
}
