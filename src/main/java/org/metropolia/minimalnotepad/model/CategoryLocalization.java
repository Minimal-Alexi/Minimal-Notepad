package org.metropolia.minimalnotepad.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * The type Category localization.
 */
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

    /**
     * Gets language.
     *
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Gets translation.
     *
     * @return the translation
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Sets language.
     *
     * @param language the language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Sets translation.
     *
     * @param translation the translation
     */
    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
