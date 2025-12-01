package dev.parhamziaei.teahub.exception.custom.service.payment;

public class InvoiceException extends RuntimeException {
    public InvoiceException() {}
    public InvoiceException(String message) {
        super(message);
    }
}
