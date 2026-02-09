package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class WalletTransactionFilterRequest extends BasePaginationRequest {

    private Long relatedResourceId;
    private TransactionType transactionType;
    private TransactionReason transactionReason;
    private LocalDateTime fromCreatedAt;
    private LocalDateTime toCreatedAt;

}
