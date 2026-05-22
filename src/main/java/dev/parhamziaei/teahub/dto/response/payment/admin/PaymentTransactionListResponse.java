package dev.parhamziaei.teahub.dto.response.payment.admin;

import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionListResponse {

    private Long id;
    private Money amount;
    private PaymentGatewayType gateway;
    private LocalDateTime transactionDate;

}
