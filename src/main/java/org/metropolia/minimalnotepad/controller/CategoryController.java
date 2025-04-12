package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.CategoryService;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final JwtUtils jwtUtils;

    public CategoryController(CategoryService categoryService, UserService userService, JwtUtils jwtUtils) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public List<Category> getAllCategories(@RequestHeader("Authorization") String authorizationHeader) {
        String token = getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);
        return categoryService.getAllCategories(user.getLanguage());
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        String token = getTokenFromHeader(authorizationHeader);
        User user = userService.getUserFromToken(token);
        return categoryService.getCategoryById(id, user.getLanguage());
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
    private String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is invalid");
        }
        return authorizationHeader.substring(7);
    }
    private User getUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        return userService.getUserByUsername(username);
    }
}
