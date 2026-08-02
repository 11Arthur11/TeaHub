package dev.parhamziaei.teahub.exception.custom.service.dns;

public class ResourceAlreadyHasAssignedSubdomainException extends RuntimeException {
    public ResourceAlreadyHasAssignedSubdomainException(String message) {
        super(message);
    }
    public ResourceAlreadyHasAssignedSubdomainException() {
        super();
    }
}
