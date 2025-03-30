package org.metropolia.minimalnotepad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.PasswordChangeRequest;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.LanguageRepository;
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

    @Autowired
    private LanguageRepository languageRepository;

    @MockitoBean
    private UserService userService;

    private User testUser;
    private String token;
    private Language testLanguage;

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    @BeforeEach
    public void setUp() {
        testLanguage = new Language();
        testLanguage.setName("en");
        testLanguage.setCountry("US");
        languageRepository.save(testLanguage);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setLanguage(testLanguage);
        testUser = userRepository.save(testUser);

        token = jwtUtils.generateToken(testUser.getUsername());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        languageRepository.deleteAll();
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

    @Test
    @WithMockUser(username = "test")
    void changeLanguage() throws Exception {
        Language language = new Language();
        language.setName("fi");
        language.setCountry("FI");
        languageRepository.save(language);

        when(userService.getUserByUsername("testUser")).thenReturn(testUser);

        mockMvc.perform(put("/api/user/change-language")
                        .header("Authorization", "Bearer " + token)
                        .param("lang", language.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Language successfully changed to Finnish"));
    }
}