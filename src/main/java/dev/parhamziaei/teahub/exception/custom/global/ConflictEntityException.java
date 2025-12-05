package dev.parhamziaei.teahub.exception.custom.global;

public class ConflictEntityException extends RuntimeException {
    public ConflictEntityException(String message) {
        super(message);
    }
}
