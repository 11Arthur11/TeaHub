package dev.parhamziaei.teahub.exception.custom.service.teaspeak;

public class YatqaServerAlreadyInitiatedException extends RuntimeException {
    public YatqaServerAlreadyInitiatedException(String message) {
        super(message);
    }

    public YatqaServerAlreadyInitiatedException() {
        super();
    }
}
