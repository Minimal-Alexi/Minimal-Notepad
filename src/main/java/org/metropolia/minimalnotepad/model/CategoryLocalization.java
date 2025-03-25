package org.metropolia.minimalnotepad.model;

import jakarta.persistence.*;

@Entity
@Table(name="categories_localizations")
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

    public String getTranslation() {
        return translation;
    }
}
