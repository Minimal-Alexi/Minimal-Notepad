package org.metropolia.minimalnotepad.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.UserService;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class UserLocaleResolver implements LocaleResolver {

    private final UserService userService;

    public UserLocaleResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

        if (username != null) {
            User user = userService.getUserByUsername(username);
            if (user != null && user.getLanguage() != null) {
                return Locale.forLanguageTag(user.getLanguage().replace("_", "-")); // Convert en_US -> en-US
            }
        }

        return Locale.forLanguageTag("en-US"); // Default locale
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // Not implemented yet, but we will need to change the user's locale in the database
    }
}

