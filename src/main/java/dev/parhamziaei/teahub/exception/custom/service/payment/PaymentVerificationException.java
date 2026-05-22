package dev.parhamziaei.teahub.exception.custom.service.payment;

public class PaymentVerificationException extends RuntimeException {
    public PaymentVerificationException(String message) {
        super(message);
    }
}
