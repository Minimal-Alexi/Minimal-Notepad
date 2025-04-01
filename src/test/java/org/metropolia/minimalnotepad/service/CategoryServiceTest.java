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

@ActiveProfiles("test")
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    private Long testCategoryId;
    private Language language;
    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Test Category #1");
        categoryRepository.save(category);
        testCategoryId = category.getId();
        language = new Language();
        language.setId(1);
    }

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteById(testCategoryId);
    }

    @Test
    @WithMockUser(username = "test")
    void getAllCategories() {
        Language language = new Language();
        language.setId(1);
        List<Category> categories = categoryService.getAllCategories();
        assertFalse(categories.isEmpty(), "The category list should not be empty");
    }

    @Test
    @WithMockUser(username = "test")
    void getCategoryById() {
        Category foundCategory = categoryService.getCategoryById(testCategoryId,language);
        assertNotNull(foundCategory, "The category should be found");
    }

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

    @Test
    @WithMockUser(username = "test")
    void updateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        categoryService.updateCategory(testCategoryId, updatedCategory);

        Category foundCategory = categoryService.getCategoryById(testCategoryId);
        assertEquals("Updated Category", foundCategory.getName(), "The category name should be updated");
    }

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
