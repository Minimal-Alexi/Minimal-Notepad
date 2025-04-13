package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.repository.LanguageRepository;
import org.springframework.stereotype.Service;

/**
 * The type Language service.
 */
@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    /**
     * Instantiates a new Language service.
     *
     * @param languageRepository the language repository
     */
    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * Gets language by id.
     *
     * @param id the id
     * @return the language by id
     */
    public Language getLanguageById(long id) {
        return languageRepository.findById(id).orElse(null);
    }

}
