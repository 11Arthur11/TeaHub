package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionFilterRequest extends BasePaginationRequest {

    @Schema(nullable = true)
    private Long relatedResourceId;
    @Schema(nullable = true)
    private TransactionType transactionType;
    @Schema(nullable = true)
    private TransactionReason transactionReason;
    @Schema(nullable = true)
    private LocalDateTime fromCreatedAt;
    @Schema(nullable = true)
    private LocalDateTime toCreatedAt;

}
