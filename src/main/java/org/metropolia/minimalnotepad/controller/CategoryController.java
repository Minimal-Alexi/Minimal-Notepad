package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.service.CategoryService;

import org.metropolia.minimalnotepad.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final LanguageService languageService;
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService, LanguageService languageService) {
        this.languageService = languageService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories(@RequestParam(required = false) Long languageId) {
        if (languageId != 1) {

            return categoryService.getAllCategories(languageService.getLanguageById(languageId));
        }
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity
                .created(URI.create("/api/categories/" + createdCategory.getId()))
                .body(createdCategory);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
