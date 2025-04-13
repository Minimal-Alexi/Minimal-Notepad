package org.metropolia.minimalnotepad.model;

import jakarta.persistence.Column;

import java.io.Serializable;

/**
 * The type Category localization id.
 */
public class CategoryLocalizationId implements Serializable {
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "language_id")
    private Long languageId;

    /**
     * Instantiates a new Category localization id.
     */
    public CategoryLocalizationId() {

    }

    /**
     * Instantiates a new Category localization id.
     *
     * @param categoryId the category id
     * @param languageId the language id
     */
    public CategoryLocalizationId(Long categoryId, Long languageId) {
        this.categoryId = categoryId;
        this.languageId = languageId;
    }

    /**
     * Gets category id.
     *
     * @return the category id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets category id.
     *
     * @param categoryId the category id
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets language id.
     *
     * @return the language id
     */
    public Long getLanguageId() {
        return languageId;
    }

    /**
     * Sets language id.
     *
     * @param languageId the language id
     */
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }
}
