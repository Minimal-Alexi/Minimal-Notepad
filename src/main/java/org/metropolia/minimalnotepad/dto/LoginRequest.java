package org.metropolia.minimalnotepad.dto;

/**
 * The type Login request.
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Instantiates a new Login request.
     *
     * @param username the username
     * @param password the password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
