package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
