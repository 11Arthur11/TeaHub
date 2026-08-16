package dev.parhamziaei.teahub.dto.request.payment.admin;

import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionAdminRequest {

    private TransactionType transactionType;
    private TransactionReason transactionReason;
    private BigDecimal amount;
    private boolean persist;

}
