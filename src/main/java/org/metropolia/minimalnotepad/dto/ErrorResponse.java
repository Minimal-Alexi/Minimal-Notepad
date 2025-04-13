package org.metropolia.minimalnotepad.dto;

import java.time.LocalDateTime;

/**
 * The type Error response.
 */
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    /**
     * Instantiates a new Error response.
     *
     * @param status  the status
     * @param message the message
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
