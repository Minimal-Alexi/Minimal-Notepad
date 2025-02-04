package org.metropolia.minimalnotepad.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String text;
    private String colour;
    @Column(name = "created_at", updatable = false, insertable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", insertable = false)
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public Note() {

    }
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getColour() {
        return colour;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
