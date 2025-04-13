package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;
    public AuthenticationService(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, MessageService messageService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    public String authenticate(String username, String password, Locale locale) {
        User findUser = userRepository.findUserByUsername(username);
        if (findUser == null) {
            String message = messageService.get("error.login.usernameNotFound", locale);
            throw new UsernameNotFoundException(message);
        }
        if (!passwordEncoder.matches(password, findUser.getPassword())) {
            String message = messageService.get("error.login.failed", locale);
            throw new BadCredentialsException(message);
        }
        return jwtUtils.generateToken(username);
    }
}
