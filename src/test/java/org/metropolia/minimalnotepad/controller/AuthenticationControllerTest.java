package org.metropolia.minimalnotepad.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.*;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.LanguageRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The type Authentication controller test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private LanguageRepository languageRepository;

    /**
     * Initial set up.
     */
    @BeforeAll
    public static void initialSetUp() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        Language language = new Language();
        language.setName("en");
        language.setId(1L);
        language.setCountry("US");
        languageRepository.save(language);
    }

    /**
     * Tear down.
     */
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        languageRepository.deleteAll();
    }

    /**
     * Test login valid user.
     */
    @Test
    public void testLoginValidUser()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");
        userMock.setLanguage(languageRepository.findByName("en").orElse(null));

        userRepository.save(userMock);

        Locale locale = Locale.forLanguageTag("en");

        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest(userMock.getUsername(), "password"), locale);

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        LoginResponse authenticationResponse = (LoginResponse) responseEntity.getBody();
        assertEquals("username", authenticationResponse.getUsername());
        assertEquals(jwtUtils.generateToken(userMock.getUsername()), authenticationResponse.getToken());
    }

    /**
     * Test login invalid password.
     */
    @Test
    public void testLoginInvalidPassword()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        userRepository.save(userMock);

        Locale locale = Locale.forLanguageTag("en");

        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest(userMock.getUsername(), "badPassword"), locale);
        assertNotNull(responseEntity);
        assertEquals(401,responseEntity.getStatusCode().value());

    }

    /**
     * Test login invalid user.
     */
    @Test
    public void testLoginInvalidUser()
    {
        Locale locale = Locale.forLanguageTag("en");
        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest("username", "badPassword"), locale);

        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCode().value());

    }

    /**
     * Test register valid user.
     */
    @Test
    public void testRegisterValidUser()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        ResponseEntity<?> responseEntity = authenticationController.register(new RegisterRequest(userMock.getUsername(),userMock.getEmail(), "password", "en"));
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) responseEntity.getBody();

        assertEquals(200,responseEntity.getStatusCode().value());
        assertEquals("username",authenticationResponse.getUsername());
    }

    /**
     * Test register taken email.
     */
    @Test
    public void testRegisterTakenEmail()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        userRepository.save(userMock);
        ResponseEntity responseEntity = authenticationController.register(new RegisterRequest(userMock.getUsername(),userMock.getEmail(), "password", "en"));
        assertNotNull(responseEntity);
        assertEquals(409,responseEntity.getStatusCode().value());
    }

    /**
     * Test register taken username.
     */
    @Test
    public void testRegisterTakenUsername()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        userRepository.save(userMock);
        ResponseEntity responseEntity = authenticationController.register(new RegisterRequest(userMock.getUsername(),userMock.getEmail(), "password", "en"));
        assertNotNull(responseEntity);
        assertEquals(409,responseEntity.getStatusCode().value());
    }

}
