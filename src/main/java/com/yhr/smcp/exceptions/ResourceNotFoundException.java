package com.yhr.smcp.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, Throwable cause) {
        super("Resource not found: " + message, cause);
    }
}
