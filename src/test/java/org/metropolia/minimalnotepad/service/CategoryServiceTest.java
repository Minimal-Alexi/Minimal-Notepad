package org.metropolia.minimalnotepad.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * The type Category service test.
 */
@ActiveProfiles("test")
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    private Long testCategoryId;
    private Language language;

    /**
     * Initial set up.
     */
    @BeforeAll
    public static void initialSetUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Test Category #1");
        categoryRepository.save(category);
        testCategoryId = category.getId();
        language = new Language();
        language.setId(1);
    }

    /**
     * Clean up.
     */
    @AfterEach
    void cleanUp() {
        categoryRepository.deleteById(testCategoryId);
    }

    /**
     * Gets all categories.
     */
    @Test
    @WithMockUser(username = "test")
    void getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        assertFalse(categories.isEmpty(), "The category list should not be empty");
    }

    /**
     * Gets category by id.
     */
    @Test
    @WithMockUser(username = "test")
    void getCategoryById() {
        Category foundCategory = categoryService.getCategoryById(testCategoryId,language);
        assertNotNull(foundCategory, "The category should be found");
    }

    /**
     * Create category.
     */
    @Test
    @WithMockUser(username = "test")
    void createCategory() {
        int categoryCount = categoryRepository.findAll().size();

        Category category = new Category();
        category.setName("Test Category #2");
        categoryService.createCategory(category);

        int newCategoryCount = categoryRepository.findAll().size();
        assertEquals(categoryCount + 1, newCategoryCount, "The category should be created");

        categoryRepository.deleteById(category.getId());
    }

    /**
     * Update category.
     */
    @Test
    @WithMockUser(username = "test")
    void updateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        categoryService.updateCategory(testCategoryId, updatedCategory);

        Category foundCategory = categoryService.getCategoryById(testCategoryId);
        assertEquals("Updated Category", foundCategory.getName(), "The category name should be updated");
    }

    /**
     * Delete category.
     */
    @Test
    @WithMockUser(username = "test")
    void deleteCategory() {
        Category category = new Category();
        category.setName("Category to Delete");
        categoryService.createCategory(category);

        categoryService.deleteCategory(category.getId());

        Category deletedCategory = categoryService.getCategoryById(category.getId());
        assertNull(deletedCategory, "The category should be deleted and not found");
    }
}
