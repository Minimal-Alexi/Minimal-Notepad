package org.metropolia.minimalnotepad.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    // Define LocaleResolver (Choose one: AcceptHeader or Session-based)
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("ru")); // Default language
        return localeResolver;
    }

    // Define MessageSource for translations
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages"); // Uses messages.properties files
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultLocale(new Locale("ru", "RU")); // Default language
        messageSource.setDefaultEncoding("UTF-8"); // Support for special characters
        return messageSource;
    }
}

