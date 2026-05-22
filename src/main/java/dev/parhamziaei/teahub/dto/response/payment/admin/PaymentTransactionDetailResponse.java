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
public class PaymentTransactionDetailResponse {

    private Long id;
    private String transactionId;
    private String trackingId;
    private Money amount;
    private String gatewayName;
    private LocalDateTime transactionDate;

}
