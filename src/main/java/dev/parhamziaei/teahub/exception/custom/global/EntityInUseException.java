package dev.parhamziaei.teahub.exception.custom.global;

public class EntityInUseException extends RuntimeException {
    public EntityInUseException(String message) {
        super(message);
    }
    public EntityInUseException() {
        super();
    }
}
