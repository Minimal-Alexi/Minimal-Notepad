package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String name);
}
