package org.metropolia.minimalnotepad.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.CategoryLocalization;
import org.metropolia.minimalnotepad.model.Language;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Category tests.
 */
public class CategoryTests {
    private Category category;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Test Category");

        Language language1 = new Language(),language2 = new Language();
        language1.setId(2);
        language2.setId(3);

        List<CategoryLocalization> localizations = new ArrayList<>();
        CategoryLocalization localization1 = new CategoryLocalization(), localization2 = new CategoryLocalization();
        localization1.setTranslation("Test 1");
        localization1.setLanguage(language1);

        localization2.setTranslation("Test 2");
        localization2.setLanguage(language2);

        localizations.add(localization1);
        localizations.add(localization2);

        category.setLocalizationList(localizations);
    }

    /**
     * Localization change successful.
     */
    @Test
    public void localizationChangeSuccessful(){
        Language language1 = new Language(),language2 = new Language();
        language1.setId(2);
        language2.setId(3);

        category.setNameToTranslation(language1);
        assertEquals("Test 1",category.getName());
        category.setNameToTranslation(language2);
        assertEquals("Test 2",category.getName());
    }

    /**
     * Localization change failed.
     */
// If localization fails, default to english.
    @Test
    public void localizationChangeFailed(){
        Language noResource = new Language();
        noResource.setId(4);
        category.setNameToTranslation(noResource);
        assertEquals("Test Category",category.getName());
    }
}
