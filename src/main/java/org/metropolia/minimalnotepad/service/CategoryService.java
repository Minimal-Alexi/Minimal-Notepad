package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Category service.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Instantiates a new Category service.
     *
     * @param categoryRepository the category repository
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Gets all categories.
     *
     * @return the all categories
     */
    @Deprecated
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Gets all categories.
     *
     * @param language the language
     * @return the all categories
     */
    public List<Category> getAllCategories(Language language) {
        List<Category> allCategories = categoryRepository.findAll();
        for (Category category : allCategories) {
            category.setNameToTranslation(language);
        }
        return allCategories;
    }

    /**
     * Gets category by id.
     *
     * @param id       the id
     * @param language the language
     * @return the category by id
     */
    public Category getCategoryById(Long id, Language language) {
        Category category =  categoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setNameToTranslation(language);
        }
        return category;
    }

    /**
     * Gets category by id.
     *
     * @param id the id
     * @return the category by id
     */
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null); // Using orElse on Optional<Category>
    }

    /**
     * Create category category.
     *
     * @param category the category
     * @return the category
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Update category category.
     *
     * @param id              the id
     * @param updatedCategory the updated category
     * @return the category
     */
    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = getCategoryById(id);
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    /**
     * Delete category.
     *
     * @param id the id
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
