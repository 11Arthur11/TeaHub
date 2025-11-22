package dev.parhamziaei.teahub.exception.custom.authentication;

public class JwtValidationException extends RuntimeException {
    public JwtValidationException(String message) {
        super(message);
    }
    public JwtValidationException() {
        super();
    }
}
