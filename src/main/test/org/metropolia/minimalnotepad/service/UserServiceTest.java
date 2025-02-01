package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserAlreadyExistsException;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUserSuccessfulRegistration() {
        User userToRegister = new User();
        userToRegister.setUsername("newUser");
        userToRegister.setPassword("encodedPassword");
        userToRegister.setEmail("newEmail@email.com");

        when(userRepository.findUserByUsername("newUser")).thenReturn(null);
        when(userRepository.findUserByEmail("newEmail@email.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(userToRegister);

        User result = userService.registerUser("newUser", "newEmail@email.com", "password");

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        assertEquals("newEmail@email.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserFailureUser() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setEmail("email@email.com");

        when(userRepository.findUserByUsername("existingUser")).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser("existingUser", "newEmail@email.com", "password");
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUserFailureEmail() {
        User existingUser = new User();
        existingUser.setUsername("username");
        existingUser.setEmail("existing@email.com");

        when(userRepository.findUserByEmail("existing@email.com")).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser("newUsername", "existing@email.com", "password");
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
