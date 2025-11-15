package dev.parhamziaei.teahub.exception.custom.authentication;

public class InvalidTwoFactorException extends AuthenticationException {
    public InvalidTwoFactorException() {
        super("Invalid two-factor");
    }
}
