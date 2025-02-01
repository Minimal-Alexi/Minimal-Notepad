package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    public AuthenticationService(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) {
        User findUser = userRepository.findUserByUsername(username);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(password, findUser.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }
        return jwtUtils.generateToken(username);
    }
}
