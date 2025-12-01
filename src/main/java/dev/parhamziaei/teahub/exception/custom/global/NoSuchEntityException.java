package dev.parhamziaei.teahub.exception.custom.global;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException(String message) {
        super(message);
    }
    public NoSuchEntityException() {
        super();
    }
}
