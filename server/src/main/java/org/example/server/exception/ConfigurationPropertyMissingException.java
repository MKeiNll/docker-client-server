package org.example.server.exception;

public class ConfigurationPropertyMissingException extends RuntimeException {
    public ConfigurationPropertyMissingException(String message) {
        super(message);
    }
}
