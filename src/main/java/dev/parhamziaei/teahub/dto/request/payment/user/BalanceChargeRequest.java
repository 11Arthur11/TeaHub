package dev.parhamziaei.teahub.dto.request.payment.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceChargeRequest {

    private BigDecimal amount;

}
