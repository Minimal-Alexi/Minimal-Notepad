package org.metropolia.minimalnotepad.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.AuthenticationResponse;
import org.metropolia.minimalnotepad.dto.LoginRequest;
import org.metropolia.minimalnotepad.dto.RegisterRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    @BeforeEach
    public void setUp() {
        Language language = new Language();
        language.setName("en");
        language.setId(1L);
        language.setCountry("US");
        languageRepository.save(language);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        languageRepository.deleteAll();
    }

    @Test
    public void testLoginValidUser()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        userRepository.save(userMock);

        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest(userMock.getUsername(), "password"));

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) responseEntity.getBody();
        assertEquals("username", authenticationResponse.getUsername());
        assertEquals(jwtUtils.generateToken(userMock.getUsername()), authenticationResponse.getToken());
    }
    @Test
    public void testLoginInvalidPassword()
    {
        User userMock = new User();
        userMock.setUsername("username");
        userMock.setPassword(passwordEncoder.encode("password"));
        userMock.setEmail("email@email.com");

        userRepository.save(userMock);

        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest(userMock.getUsername(), "badPassword"));
        assertNotNull(responseEntity);
        assertEquals(401,responseEntity.getStatusCode().value());

    }
    @Test
    public void testLoginInvalidUser()
    {
        ResponseEntity<?> responseEntity = authenticationController.login(new LoginRequest("username", "badPassword"));

        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCode().value());

    }
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
