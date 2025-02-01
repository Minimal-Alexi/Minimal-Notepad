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

    public String authenticate(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User findUser = userRepository.findUserByUsername(username);
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!encodedPassword.equals(findUser.getPassword()))
        {
            throw new BadCredentialsException("Bad credentials");
        }
        return jwtUtils.generateToken(username);
    }
}
