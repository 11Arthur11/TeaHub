package dev.parhamziaei.teahub.exception.custom.service.resource;

public class ResourceSuspendedException extends RuntimeException {
    public ResourceSuspendedException(String message) {
        super(message);
    }
}
