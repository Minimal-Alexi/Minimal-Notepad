package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
