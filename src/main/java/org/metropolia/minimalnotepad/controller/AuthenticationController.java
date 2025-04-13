package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.dto.AuthenticationResponse;
import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.LoginRequest;
import org.metropolia.minimalnotepad.dto.RegisterRequest;
import org.metropolia.minimalnotepad.dto.LoginResponse;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.AuthenticationService;
import org.metropolia.minimalnotepad.service.MessageService;
import org.metropolia.minimalnotepad.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/users-authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final MessageService messageService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService, MessageService messageService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.messageService = messageService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            String jwt = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword(), locale);
            User user = userService.getUserByUsername(loginRequest.getUsername());
            String language = user.getLanguage().getName();
            return ResponseEntity.ok(new LoginResponse(jwt, loginRequest.getUsername(), language));
        } catch (Exception e) {
            if (e instanceof UsernameNotFoundException) {
                String message = messageService.get("error.login.usernameNotFound", locale);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), message));
            }

            String message = messageService.get("error.login.failed", locale);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = userService.registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getLanguage());
            Locale locale = new Locale(registerRequest.getLanguage());
            String jwt = authenticationService.authenticate(registeredUser.getUsername(), registerRequest.getPassword(), locale);
            return ResponseEntity.ok(new AuthenticationResponse(jwt, registerRequest.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
        }
    }
}
