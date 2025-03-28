package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public List<Category> getAllCategories(Language language) {
        List<Category> allCategories = categoryRepository.findAll();
        if (language.getId() != 1) {
            for (Category category : allCategories) {
                category.setNameToTranslation(language);
            }
        }
        return allCategories;
    }

    public Category getCategoryById(Long id, Language language) {
        return categoryRepository.findById(id).orElse(null); // Using orElse on Optional<Category>
    }
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null); // Using orElse on Optional<Category>
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = getCategoryById(id);
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
