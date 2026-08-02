package dev.parhamziaei.teahub.exception.custom.service.dns;

public class ZoneDisabledException extends RuntimeException {
    public ZoneDisabledException(String message) {
        super(message);
    }
}
