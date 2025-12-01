package dev.parhamziaei.teahub.exception.custom.service.user;

import java.math.BigDecimal;

public class WalletChargeAmountTooSmallException extends RuntimeException {
    private final BigDecimal amount;
    public WalletChargeAmountTooSmallException(BigDecimal amount) {
        this.amount = amount;
    }
    public WalletChargeAmountTooSmallException(String message, BigDecimal amount) {
        super(message);
        this.amount = amount;
    }
}
