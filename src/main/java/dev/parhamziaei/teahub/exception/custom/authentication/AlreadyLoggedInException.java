package dev.parhamziaei.teahub.exception.custom.authentication;

public class AlreadyLoggedInException extends RuntimeException{
    public AlreadyLoggedInException() {
        super("ALREADY_LOGGED_IN");
    }
}
