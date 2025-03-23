package org.metropolia.minimalnotepad.config;
import org.metropolia.minimalnotepad.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class MessageSourceConfig {

    @Bean
    public LocaleResolver localeResolver(UserService userService) {
        return new UserLocaleResolver(userService); // Use custom resolver
    }

    // Define MessageSource for translations
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages"); // Uses messages.properties files
        messageSource.setDefaultEncoding("UTF-8"); // Support for special characters
        messageSource.setUseCodeAsDefaultMessage(true); // If no translation is found, return the key
        return messageSource;
    }
}

