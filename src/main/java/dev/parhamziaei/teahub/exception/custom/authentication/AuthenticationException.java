package dev.parhamziaei.teahub.exception.custom.authentication;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {}
    public AuthenticationException(String message) {
        super(message);
    }
}
