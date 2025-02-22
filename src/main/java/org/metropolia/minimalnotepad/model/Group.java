package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"email", "notes", "password", "groupParticipationsList", "enabled", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired","groups"})
    private User user;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("group-reference")
    private List<Note> notes;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("participation-group")
    private List<UserGroupParticipation> userGroupParticipationsList;

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
    public String getDescription(){
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<Note> getNotes(){
        return notes;
    }
    public void setNotes(List<Note> notes){
        this.notes = notes;
    }
    public List<UserGroupParticipation> getUserGroupParticipationsList(){
        return userGroupParticipationsList;
    }
}