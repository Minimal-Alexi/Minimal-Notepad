package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("figure-reference")
    private List<Figure> figures;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"email", "notes", "password", "groupParticipationsList"})
    private User user;
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"notes"})
    private Group group;
    @ManyToMany
    @JoinTable(name = "note_categories",
    joinColumns = @JoinColumn(name = "note_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<Category> categoriesList;

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
    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }
    public List<Figure> getFigures() {
        return figures;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public Group getGroup() {
        return group;
    }
    public void setCategoriesList(List<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }
    public List<Category> getCategoriesList() {
        return categoriesList;
    }
    public void categoryLocalization(Language language) {
        if (categoriesList != null) {
            for (Category category : categoriesList) {
                category.setNameToTranslation(language);
            }
        }
    }

    public String getCategory() {
        if (categoriesList != null && !categoriesList.isEmpty()) {
            return categoriesList.stream()
                    .map(Category::getName) // Assuming Category has a getName() method
                    .collect(Collectors.joining(", "));
        }
        return "Uncategorized";
    }

}
