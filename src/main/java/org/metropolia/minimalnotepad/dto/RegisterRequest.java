package org.metropolia.minimalnotepad.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    private String language;
    public RegisterRequest( String username,String email, String password, String language) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.language = language;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public String getLanguage() {
        return language;
    }
}
