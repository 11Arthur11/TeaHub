package dev.parhamziaei.teahub.exception.custom.service.payment;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) {
        super(message);
    }
    public PaymentFailedException() {}
}
