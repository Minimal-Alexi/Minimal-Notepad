package org.metropolia.minimalnotepad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.PasswordChangeRequest;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    // Get User Account
    @GetMapping("/")
    public ResponseEntity<?> getUserAccount(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(currentUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    // Delete User Account
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUserAccount(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            userService.deleteUser(currentUser.getId());
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.OK.value(), "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    // Update User (Username & Email)
    @PutMapping("/")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody User updatedUser) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            if (updatedUser.getUsername() == null || updatedUser.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Username is required"));
            }

            if (updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Email is required"));
            }

            if (updatedUser.getUsername().equals(currentUser.getUsername()) && updatedUser.getEmail().equals(currentUser.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "No changes detected"));
            }

            boolean isUsernameChanged = !currentUser.getUsername().equals(updatedUser.getUsername());

            User updated = userService.updateUser(currentUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.putAll(new ObjectMapper().convertValue(updated, Map.class));

            // ✅ Only generate and include a new token if the username was changed
            if (isUsernameChanged) {
                String newToken = jwtUtils.generateToken(updated.getUsername());
                response.put("token", newToken);
            }

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // Change Password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            if (passwordChangeRequest.getOldPassword() == null || passwordChangeRequest.getOldPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Old password is required"));
            }

            if (passwordChangeRequest.getNewPassword() == null || passwordChangeRequest.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "New password is required"));
            }

            if (passwordChangeRequest.getConfirmPassword() == null || passwordChangeRequest.getConfirmPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Confirm password is required"));
            }

            if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "New password and confirm password do not match"));
            }

            if (passwordChangeRequest.getOldPassword().equals(passwordChangeRequest.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "New password is the same as the old password"));
            }

            userService.changePassword(currentUser.getId(), passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.OK.value(), "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // Change Language
    @PutMapping("/change-language")
    public ResponseEntity<?> changeLanguage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String lang) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            if (lang == null || lang.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Language code is required"));
            }

            String languageName;
            switch (lang.trim().toLowerCase()) {
                case "en":
                    languageName = "English";
                    break;
                case "fi":
                    languageName = "Finnish";
                    break;
                case "ru":
                    languageName = "Russian";
                    break;
                case "zh":
                    languageName = "Chinese";
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid language code"));
            }

            if (lang.equals(currentUser.getLanguage().getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Language is already set to " + languageName));
            }

            userService.updateUserLanguage(currentUser.getId(), lang);
            return ResponseEntity.ok(new ErrorResponse(HttpStatus.OK.value(), "Language successfully changed to " + languageName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    private String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is invalid");
        }
        return authorizationHeader.substring(7);
    }

    private User getUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        return userService.getUserByUsername(username);
    }
}
