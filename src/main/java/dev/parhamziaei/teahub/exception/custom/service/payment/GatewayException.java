package dev.parhamziaei.teahub.exception.custom.service.payment;

public class GatewayException extends RuntimeException {
    public GatewayException(String message) {
        super(message);
    }
}
