package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Note.
 */
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
    /**
     * The Categories list.
     */
    @ManyToMany
    @JoinTable(name = "note_categories",
    joinColumns = @JoinColumn(name = "note_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<Category> categoriesList;

    /**
     * Instantiates a new Note.
     */
    public Note() {

    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
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
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets colour.
     *
     * @param colour the colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Gets colour.
     *
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets created at.
     *
     * @param createdAt the created at
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets updated at.
     *
     * @param updatedAt the updated at
     */
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets updated at.
     *
     * @return the updated at
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets figures.
     *
     * @param figures the figures
     */
    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    /**
     * Gets figures.
     *
     * @return the figures
     */
    public List<Figure> getFigures() {
        return figures;
    }

    /**
     * Sets group.
     *
     * @param group the group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets group.
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets categories list.
     *
     * @param categoriesList the categories list
     */
    public void setCategoriesList(List<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }

    /**
     * Gets categories list.
     *
     * @return the categories list
     */
    public List<Category> getCategoriesList() {
        return categoriesList;
    }

    /**
     * Category localization.
     *
     * @param language the language
     */
    public void categoryLocalization(Language language) {
        if (categoriesList != null) {
            for (Category category : categoriesList) {
                category.setNameToTranslation(language);
            }
        }
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        if (categoriesList != null && !categoriesList.isEmpty()) {
            return categoriesList.stream()
                    .map(Category::getName) // Assuming Category has a getName() method
                    .collect(Collectors.joining(", "));
        }
        return "Uncategorized";
    }

}
