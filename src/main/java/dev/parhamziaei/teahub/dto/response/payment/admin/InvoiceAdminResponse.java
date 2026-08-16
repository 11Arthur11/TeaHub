package dev.parhamziaei.teahub.dto.response.payment.admin;

import dev.parhamziaei.teahub.dto.response.user.admin.UserDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.user.admin.UserListResponse;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.valueobject.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class InvoiceAdminResponse {

    private String invoiceToken;
    
    private Money money;

    private Integer taxPercentage;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    private InvoiceStatus status;

    private PaymentTransactionDetailResponse paymentTransaction;

    private String ownerFullName;

}
