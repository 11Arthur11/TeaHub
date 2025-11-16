package dev.parhamziaei.teahub.exception.custom.authentication;

public class PhoneNumberAlreadyTakenException extends AuthenticationException{
    public PhoneNumberAlreadyTakenException() {
        super("EMAIL_ALREADY_TAKEN");
    }
}
