package org.metropolia.minimalnotepad.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories_localizations")
public class CategoryLocalization {
    @EmbeddedId
    private CategoryLocalizationId id = new CategoryLocalizationId();
    @ManyToOne
    @MapsId("category_id")
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @MapsId("language_id")
    @JoinColumn(name = "language_id")
    private Language language;
    private String translation;

    public Language getLanguage() {
        return language;
    }
    public String getTranslation() {
        return translation;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
