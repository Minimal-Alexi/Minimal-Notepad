package org.metropolia.minimalnotepad.model;

import jakarta.persistence.Column;

import java.io.Serializable;

public class CategoryLocalizationId implements Serializable {
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "language_id")
    private Long languageId;
    public CategoryLocalizationId() {

    }
    public CategoryLocalizationId(Long categoryId, Long languageId) {
        this.categoryId = categoryId;
        this.languageId = languageId;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public Long getLanguageId() {
        return languageId;
    }
    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }
}
