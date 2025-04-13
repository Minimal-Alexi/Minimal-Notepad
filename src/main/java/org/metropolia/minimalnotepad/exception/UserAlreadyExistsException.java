package org.metropolia.minimalnotepad.exception;

/**
 * The type User already exists exception.
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Instantiates a new User already exists exception.
     *
     * @param message the message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
