package dev.parhamziaei.teahub.exception.custom.authentication;

public class EmailAlreadyTakenException extends AuthenticationException{
    public EmailAlreadyTakenException() {
        super("EMAIL_ALREADY_TAKEN");
    }
}
