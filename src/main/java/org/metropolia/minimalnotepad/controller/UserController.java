package org.metropolia.minimalnotepad.controller;

import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.PasswordChangeRequest;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            return ResponseEntity.ok(currentUser);
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

            User updated = userService.updateUser(currentUser.getId(), updatedUser.getUsername(), updatedUser.getEmail());

            return ResponseEntity.ok(updated);
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
