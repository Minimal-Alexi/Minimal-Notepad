package org.metropolia.minimalnotepad.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String languageCode;
    public LoginResponse(String token, String username, String languageCode) {
        this.token = token;
        this.username = username;
        this.languageCode = languageCode;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public String getLanguageCode() { return languageCode; }
}
