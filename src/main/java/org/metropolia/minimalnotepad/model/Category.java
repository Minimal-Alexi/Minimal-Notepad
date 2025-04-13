package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

/**
 * The type Category.
 */
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "categoriesList")
    @JsonManagedReference("category-note")
    @JsonIgnore
    private List<Note> noteList;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CategoryLocalization> localizationList;

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

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets note list.
     *
     * @return the note list
     */
    public List<Note> getNoteList() {
        return noteList;
    }

    /**
     * Sets note list.
     *
     * @param noteList the note list
     */
    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    /**
     * Gets localization list.
     *
     * @return the localization list
     */
    public List<CategoryLocalization> getLocalizationList() {
        return localizationList;
    }

    /**
     * Sets localization list.
     *
     * @param localizationList the localization list
     */
    public void setLocalizationList(List<CategoryLocalization> localizationList) {
        this.localizationList = localizationList;
    }

    /**
     * Sets name to translation.
     *
     * @param language the language
     */
    public void setNameToTranslation(Language language) {
        if (language != null && language.getId() != 1 && localizationList != null) {
            for (CategoryLocalization localization : localizationList) {
                if (localization.getLanguage().getId() == language.getId()) {
                    name = localization.getTranslation();
                    return;
                }
            }
        }
    }
}
