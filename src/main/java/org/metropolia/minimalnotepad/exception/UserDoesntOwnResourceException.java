package org.metropolia.minimalnotepad.exception;

public class UserDoesntOwnResourceException extends RuntimeException {
    public UserDoesntOwnResourceException(String message) {
        super(message);
    }
}
