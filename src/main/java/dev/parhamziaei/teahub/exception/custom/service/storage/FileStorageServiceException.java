package dev.parhamziaei.teahub.exception.custom.service.storage;

public class FileStorageServiceException extends RuntimeException {
    public FileStorageServiceException(String message) {
        super(message);
    }
    public FileStorageServiceException(){
        super();
    }
}
