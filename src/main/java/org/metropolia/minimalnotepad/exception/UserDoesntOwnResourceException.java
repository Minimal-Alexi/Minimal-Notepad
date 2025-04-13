package org.metropolia.minimalnotepad.exception;

/**
 * The type User doesnt own resource exception.
 */
public class UserDoesntOwnResourceException extends RuntimeException {
    /**
     * Instantiates a new User doesnt own resource exception.
     *
     * @param message the message
     */
    public UserDoesntOwnResourceException(String message) {
        super(message);
    }
}
