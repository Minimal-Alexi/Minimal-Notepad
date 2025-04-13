package org.metropolia.minimalnotepad.dto;

/**
 * The type Authentication response.
 */
public class AuthenticationResponse {
    private String token;
    private String username;

    /**
     * Instantiates a new Authentication response.
     *
     * @param token    the token
     * @param username the username
     */
    public AuthenticationResponse(String token, String username) {
        this.token = token;
        this.username = username;
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
}
