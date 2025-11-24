package dev.parhamziaei.teahub.exception.custom.service.teaspeak;

public class InstancePortRangeNotValidException extends RuntimeException {
    public InstancePortRangeNotValidException(String message) {
        super(message);
    }
    public InstancePortRangeNotValidException() {
        super();
    }
}
