package dev.parhamziaei.teahub.exception.custom.service.storage;

public class MediaTypeNotAllowedException extends FileStorageServiceException {
    public MediaTypeNotAllowedException(String message) {
        super(message);
    }
    public MediaTypeNotAllowedException() {
        super();
    }
}
