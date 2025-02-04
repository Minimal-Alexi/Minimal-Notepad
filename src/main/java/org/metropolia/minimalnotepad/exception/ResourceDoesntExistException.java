package org.metropolia.minimalnotepad.exception;

public class ResourceDoesntExistException extends RuntimeException {
    public ResourceDoesntExistException(String message) {
        super(message);
    }
}
