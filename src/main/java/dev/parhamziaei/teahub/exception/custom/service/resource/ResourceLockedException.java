package dev.parhamziaei.teahub.exception.custom.service.resource;

public class ResourceLockedException extends RuntimeException {
    public ResourceLockedException(String message) {
        super(message);
    }
    public ResourceLockedException() {
        super();
    }
}
