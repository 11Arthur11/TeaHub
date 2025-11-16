package dev.parhamziaei.teahub.exception.custom.authentication;

public class InvalidTwoFactorException extends AuthenticationException {
    public InvalidTwoFactorException(String message) {
        super(message);
    }
    public InvalidTwoFactorException() {
        super();
    }
}
