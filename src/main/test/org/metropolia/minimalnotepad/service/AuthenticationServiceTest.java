package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.config.H2TestConfig;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import(H2TestConfig.class)
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
    public void testAuthenticate()
    {
        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword123");

        when(userRepository.findUserByUsername("testuser")).thenReturn(mockUser);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(jwtUtils.generateToken(mockUser.getUsername())).thenReturn("mockToken");

        String token = authService.authenticate("testuser", "password123");

        assertNotNull(token);
        assertEquals("mockToken", token);

        verify(userRepository).findUserByUsername("testuser");
        verify(jwtUtils).generateToken(mockUser.getUsername());
    }
}
