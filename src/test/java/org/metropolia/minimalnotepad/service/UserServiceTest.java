package org.metropolia.minimalnotepad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metropolia.minimalnotepad.exception.UserAlreadyExistsException;
import org.metropolia.minimalnotepad.exception.UserNotFoundException;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

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
    @Test
    public void testGetUserByUsername(){
        User user = new User();
        user.setUsername("username");
        user.setEmail("email@email.com");

        when(userRepository.findUserByUsername("username")).thenReturn(user);

        User result = userService.getUserByUsername("username");
        assertEquals(user,result);
        assertEquals("username",result.getUsername());
        assertEquals("email@email.com",result.getEmail());
    }

    @Test
    public void testUpdateUserSuccess() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@email.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newUsername");
        updatedUser.setEmail("newEmail@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByUsername("newUsername")).thenReturn(null);
        when(userRepository.findUserByEmail("newEmail@email.com")).thenReturn(null);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        User result = userService.updateUser(1L, "newUsername", "newEmail@email.com");

        assertEquals("newUsername", result.getUsername());
        assertEquals("newEmail@email.com", result.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, "newUsername", "newEmail@email.com");
        });
    }

    @Test
    public void testUpdateUserUsernameAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@email.com");

        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("newUsername");
        anotherUser.setEmail("otherEmail@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByUsername("newUsername")).thenReturn(anotherUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(1L, "newUsername", "newEmail@email.com");
        });
    }

    @Test
    public void testUpdateUserEmailAlreadyExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setEmail("oldEmail@email.com");

        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("otherUsername");
        anotherUser.setEmail("newEmail@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByEmail("newEmail@email.com")).thenReturn(anotherUser);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(1L, "newUsername", "newEmail@email.com");
        });
    }

    @Test
    public void testChangePasswordSuccess() {
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");
        when(passwordEncoder.matches("oldPassword", "hashedOldPassword")).thenReturn(true);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@email.com");
        user.setPassword("hashedOldPassword"); // Use expected hashed password

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.changePassword(1L, "oldPassword", "newPassword");

        assertEquals("hashedNewPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testChangePasswordUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword(1L, "oldPassword", "newPassword");
        });
    }

    @Test
    public void testChangePasswordIncorrectOldPassword() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testUser");
        existingUser.setPassword(passwordEncoder.encode("oldPassword"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.changePassword(1L, "incorrectOldPassword", "newPassword");
        });
    }

    @Test
    public void deleteUserTest() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertTrue(userRepository.findById(userId).isEmpty());
    }
}
