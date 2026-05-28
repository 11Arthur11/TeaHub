package dev.parhamziaei.teahub.dto.request.payment.admin;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDebtInvoiceRequest {

    private Long targetUserId;
    private Money amount;
    private String description;

}
