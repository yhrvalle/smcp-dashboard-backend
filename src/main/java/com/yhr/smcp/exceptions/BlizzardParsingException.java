package com.yhr.smcp.exceptions;

public class BlizzardParsingException extends RuntimeException {
    public BlizzardParsingException(String entity, String field, Throwable cause) {
        super("Failed to parse " + entity + " at: " + field + " cause: " + cause.getMessage(), cause);

    }
}
