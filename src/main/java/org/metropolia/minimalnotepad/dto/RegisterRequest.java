package org.metropolia.minimalnotepad.dto;

/**
 * The type Register request.
 */
public class RegisterRequest {
    private String email;
    private String password;
    private String username;
    private String language;

    /**
     * Instantiates a new Register request.
     *
     * @param username the username
     * @param email    the email
     * @param password the password
     * @param language the language
     */
    public RegisterRequest(String username, String email, String password, String language) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.language = language;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
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
     * Gets language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
}
