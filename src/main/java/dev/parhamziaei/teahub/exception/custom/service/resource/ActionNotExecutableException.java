package dev.parhamziaei.teahub.exception.custom.service.resource;

public class ActionNotExecutableException extends RuntimeException {
    public ActionNotExecutableException(String message) {
        super(message);
    }
}
