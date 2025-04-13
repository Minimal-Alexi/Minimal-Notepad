package org.metropolia.minimalnotepad.exception;

/**
 * The type User not found exception.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Instantiates a new User not found exception.
     *
     * @param message the message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
