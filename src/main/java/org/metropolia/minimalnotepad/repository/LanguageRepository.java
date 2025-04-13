package org.metropolia.minimalnotepad.repository;

import org.metropolia.minimalnotepad.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Language repository.
 */
public interface LanguageRepository extends JpaRepository<Language, Long> {
    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Language> findByName(String name);
}
