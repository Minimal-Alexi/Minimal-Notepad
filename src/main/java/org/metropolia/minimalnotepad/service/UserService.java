package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserAlreadyExistsException;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.exception.UserNotFoundException;

import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }
    public User registerUser(String username, String email, String password) {
        if(userRepository.findUserByEmail(email) != null)
        {
            throw new UserAlreadyExistsException("Email already exists.");
        }
        if(userRepository.findUserByUsername(username) != null)
        {
            throw new UserAlreadyExistsException("Username already exists.");
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

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
