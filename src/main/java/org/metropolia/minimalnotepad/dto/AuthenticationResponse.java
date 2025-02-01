package org.metropolia.minimalnotepad.dto;

public class AuthenticationResponse {
    private String token;
    private String username;
    public AuthenticationResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
}
