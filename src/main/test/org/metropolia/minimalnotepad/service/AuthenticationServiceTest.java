package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    public void testAuthenticateValidAttempt()
    {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword123");

        when(userRepository.findUserByUsername("testuser")).thenReturn(mockUser);
        when(passwordEncoder.matches("password123","encodedPassword123")).thenReturn(true);
        when(jwtUtils.generateToken(mockUser.getUsername())).thenReturn("mockToken");

        String token = authService.authenticate("testuser", "password123");

        assertNotNull(token);
        assertEquals("mockToken", token);

        verify(userRepository).findUserByUsername("testuser");
        verify(jwtUtils).generateToken(mockUser.getUsername());
    }
    @Test
    public void testAuthenticateUserNotFound()
    {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword123");
        when(userRepository.findUserByUsername("testuser")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate("testuser", "password123"));
    }
}
