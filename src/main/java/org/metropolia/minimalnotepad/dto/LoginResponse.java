package org.metropolia.minimalnotepad.dto;

/**
 * The type Login response.
 */
public class LoginResponse {
    private String token;
    private String username;
    private String languageCode;

    /**
     * Instantiates a new Login response.
     *
     * @param token        the token
     * @param username     the username
     * @param languageCode the language code
     */
    public LoginResponse(String token, String username, String languageCode) {
        this.token = token;
        this.username = username;
        this.languageCode = languageCode;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
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
     * Gets language code.
     *
     * @return the language code
     */
    public String getLanguageCode() {
        return languageCode;
    }
}
