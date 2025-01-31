package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {

        return jwtUtils.generateToken(username);
    }
}
