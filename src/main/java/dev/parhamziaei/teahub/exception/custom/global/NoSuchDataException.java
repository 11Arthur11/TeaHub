package dev.parhamziaei.teahub.exception.custom.global;

public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException(String message) {
        super(message);
    }
    public NoSuchDataException() {
        super();
    }
}
