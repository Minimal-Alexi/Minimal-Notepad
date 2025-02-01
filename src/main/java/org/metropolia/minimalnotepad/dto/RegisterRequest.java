package org.metropolia.minimalnotepad.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    public RegisterRequest( String username,String email, String password) {
        this.email = email;
        this.password = password;
        this.username = username;
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
}
