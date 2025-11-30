package dev.parhamziaei.teahub.service.interfaces;

import java.math.BigDecimal;

public interface PaymentService {
    String createInvoice(Long userId, BigDecimal amount);
}
