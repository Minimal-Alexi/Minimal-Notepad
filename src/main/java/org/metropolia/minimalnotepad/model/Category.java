package org.metropolia.minimalnotepad.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToMany(mappedBy = "categoriesList")
    @JsonManagedReference("category-note")
    @JsonIgnore
    private List<Note> noteList;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CategoryLocalization> localizationList;

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
    public List<Note> getNoteList() {
        return noteList;
    }
    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
    public List<CategoryLocalization> getLocalizationList() {
        return localizationList;
    }
    public void setLocalizationList(List<CategoryLocalization> localizationList) {
        this.localizationList = localizationList;
    }
    public void setNameToTranslation(Language language) {
        if(language != null && language.getId() != 1 && localizationList != null) {
            for(CategoryLocalization localization : localizationList) {
                if(localization.getLanguage().getId() == language.getId()) {
                    name = localization.getTranslation();
                    return;
                }
            }
        }
    }
}
