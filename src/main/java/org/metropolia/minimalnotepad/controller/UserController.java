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
import org.springframework.web.bind.annotation.*;

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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(currentUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }

    // Delete User Account
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUserAccount(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User currentUser = getUserFromToken(token);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            userService.deleteUser(currentUser.getId());
            return ResponseEntity.ok(new ErrorResponse(200, "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            if (updatedUser.getUsername() == null || updatedUser.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Username is required"));
            }

            if (updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Email is required"));
            }

            if (updatedUser.getUsername().equals(currentUser.getUsername()) && updatedUser.getEmail().equals(currentUser.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "No changes detected"));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            if (passwordChangeRequest.getOldPassword() == null || passwordChangeRequest.getOldPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Old password is required"));
            }

            if (passwordChangeRequest.getNewPassword() == null || passwordChangeRequest.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "New password is required"));
            }

            if (passwordChangeRequest.getConfirmPassword() == null || passwordChangeRequest.getConfirmPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Confirm password is required"));
            }

            if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "New password and confirm password do not match"));
            }

            if (passwordChangeRequest.getOldPassword().equals(passwordChangeRequest.getNewPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "New password is the same as the old password"));
            }

            userService.changePassword(currentUser.getId(), passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
            return ResponseEntity.ok(new ErrorResponse(200, "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            if (lang == null || lang.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Language code is required"));
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
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Invalid language code"));
            }

            if (lang.equals(currentUser.getLanguage().getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Language is already set to " + languageName));
            }

            userService.updateUserLanguage(currentUser.getId(), lang);
            return ResponseEntity.ok(new ErrorResponse(200, "Language successfully changed to " + languageName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
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
