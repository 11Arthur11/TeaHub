package dev.parhamziaei.teahub.exception.custom.authentication;

public class InvalidEmailVerifyCodeException extends AuthenticationException {
    public InvalidEmailVerifyCodeException() {
        super("Email verification code is invalid");
    }
}
