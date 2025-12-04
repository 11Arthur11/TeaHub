package dev.parhamziaei.teahub.exception.custom.service.payment;

public class GatewayNotFoundException extends RuntimeException {
    public GatewayNotFoundException(String message) {
        super(message);
    }
    public GatewayNotFoundException() {
    }
}
