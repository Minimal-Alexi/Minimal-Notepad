package org.metropolia.minimalnotepad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.PasswordChangeRequest;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper; // Used for JSON conversion

    @Autowired
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserService userService;

    private User testUser;
    private String token;


    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);
        token = jwtUtils.generateToken(testUser.getUsername());
    }

    @Test
    @WithMockUser(username = "test")
    void getUserAccount() throws Exception {
        when(userService.getUserByUsername("testUser")).thenReturn(testUser);

        mockMvc.perform(get("/api/user/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserByUsername("testUser");
    }

    @Test
    @WithMockUser(username = "test")
    void deleteUserAccount() throws Exception {
        when(userService.getUserByUsername("testUser")).thenReturn(testUser);

        mockMvc.perform(delete("/api/user/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @WithMockUser(username = "test")
    void updateUser() throws Exception {
        when(userService.getUserByUsername("testUser")).thenReturn(testUser);

        String updatedUsername = "updatedUser";
        String updatedEmail = "updated@example.com";

        User updatedUser = new User();
        updatedUser.setUsername(updatedUsername);
        updatedUser.setEmail(updatedEmail);

        when(userService.updateUser(testUser.getId(), updatedUsername, updatedEmail)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedUsername))
                .andExpect(jsonPath("$.email").value(updatedEmail));
    }

    @Test
    @WithMockUser(username = "test")
    void changePassword() throws Exception {
        String oldPassword = "password";
        String newPassword = "newSecurePassword123";
        String confirmPassword = "newSecurePassword123";

        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setOldPassword(oldPassword);
        passwordChangeRequest.setNewPassword(newPassword);
        passwordChangeRequest.setConfirmPassword(confirmPassword);

        when(userService.getUserByUsername("testUser")).thenReturn(testUser);
        doNothing().when(userService).changePassword(testUser.getId(), oldPassword, newPassword);

        mockMvc.perform(put("/api/user/change-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordChangeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        verify(userService, times(1)).changePassword(testUser.getId(), oldPassword, newPassword);
    }
}