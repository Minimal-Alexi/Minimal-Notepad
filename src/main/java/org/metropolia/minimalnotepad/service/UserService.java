package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserAlreadyExistsException;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
