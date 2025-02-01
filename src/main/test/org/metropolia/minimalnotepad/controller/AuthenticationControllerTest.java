package org.metropolia.minimalnotepad.controller;

import org.junit.jupiter.api.BeforeEach;
import org.metropolia.minimalnotepad.dto.AuthenticationResponse;
import org.metropolia.minimalnotepad.dto.LoginRequest;
import org.metropolia.minimalnotepad.model.User;

import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
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
        assertNotNull(authenticationResponse.getToken());
    }
    @Test
    public void testLoginInvalidPassword()
    {


    }
    @Test
    public void testLoginInvalidUser()
    {

    }
    @Test
    public void testRegisterValidUser()
    {

    }
    @Test
    public void testRegisterTakenEmail()
    {

    }
    @Test
    public void testRegisterTakenUsername()
    {

    }

}
