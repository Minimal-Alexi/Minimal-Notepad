package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.repository.LanguageRepository;
import org.springframework.stereotype.Service;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;
    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }
    public Language getLanguageById(long id) {
        return languageRepository.findById(id).orElse(null);
    }

}
