package org.metropolia.minimalnotepad.exception;

public class UserDoesntOwnResource extends RuntimeException {
    public UserDoesntOwnResource(String message) {
        super(message);
    }
}
