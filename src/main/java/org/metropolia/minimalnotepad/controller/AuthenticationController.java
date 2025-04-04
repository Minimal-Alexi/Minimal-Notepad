package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.dto.AuthenticationResponse;
import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.LoginRequest;
import org.metropolia.minimalnotepad.dto.RegisterRequest;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.AuthenticationService;
import org.metropolia.minimalnotepad.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users-authentication")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try
        {
            String jwt = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new AuthenticationResponse(jwt, loginRequest.getUsername()));
        }catch (Exception e)
        {
            if(e instanceof UsernameNotFoundException)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try
        {
            User registeredUser = userService.registerUser(registerRequest.getUsername(),registerRequest.getEmail(),registerRequest.getPassword(),registerRequest.getLanguage());
            String jwt = authenticationService.authenticate(registeredUser.getUsername(),registerRequest.getPassword());
            return ResponseEntity.ok(new AuthenticationResponse(jwt, registerRequest.getUsername()));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, e.getMessage()));
        }
    }
}
