package dev.parhamziaei.teahub.dto.response.user;

import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionResponse {

    private Long relatedResourceId;

    private TransactionReason reason;

    private TransactionType type;

    private LocalDateTime createdAt;

    private Money amount;

}
