package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserAlreadyExistsException;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.LanguageRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.exception.UserNotFoundException;

import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final LanguageRepository languageRepository;
    private final MessageService messageService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, LanguageRepository languageRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.languageRepository = languageRepository;
        this.messageService = messageService;
    }
    public User registerUser(String username, String email, String password, String languageName) {
        Locale locale = new Locale(languageName);
        if(userRepository.findUserByEmail(email) != null)
        {
            String message = messageService.get("error.register.emailExists", locale);
            throw new UserAlreadyExistsException(message);
        }
        if(userRepository.findUserByUsername(username) != null)
        {
            String message = messageService.get("error.register.usernameExists", locale);
            throw new UserAlreadyExistsException(message);
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        Language language = languageRepository.findByName(languageName.trim().toLowerCase())
                .orElseGet(() -> languageRepository.findByName("en").orElse(null));

        user.setLanguage(language);

        return userRepository.save(user);
    }
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User updateUser(Long userId, String newUsername, String newEmail) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        User user = existingUser.get();

        if (!user.getUsername().equals(newUsername) && userRepository.findUserByUsername(newUsername) != null) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        if (!user.getEmail().equals(newEmail) && userRepository.findUserByEmail(newEmail) != null) {
            throw new UserAlreadyExistsException("Email already taken.");
        }

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        User user = existingUser.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateUserLanguage(Long userId, String languageName) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        User user = existingUser.get();

        if (languageName == null || languageName.isBlank()) {
            throw new IllegalArgumentException("Language name cannot be empty.");
        }

        String normalizedLanguageName = languageName.trim().toLowerCase();

        Language language = languageRepository.findByName(normalizedLanguageName)
                .orElseThrow(() -> new IllegalArgumentException("Language '" + normalizedLanguageName + "' not found."));

        user.setLanguage(language);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        userRepository.deleteById(userId);
    }

    public User getUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        return this.getUserByUsername(username);
    }
}
