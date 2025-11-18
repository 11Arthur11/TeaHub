package dev.parhamziaei.teahub.exception.custom.service;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message) {
        super(message);
    }
    public JwtValidationException() {
        super();
    }
}
