package dev.parhamziaei.teahub.exception.custom.service.resource;

public class ResourceProvisionException extends RuntimeException {
    public ResourceProvisionException(String message) {
        super(message);
    }

    public ResourceProvisionException() {
        super();
    }
}
