package dev.parhamziaei.teahub.exception.custom.service.payment;

import lombok.Getter;

public class PaymentFailedException extends RuntimeException {
    @Getter
    private final Long invoiceId;
    public PaymentFailedException(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
