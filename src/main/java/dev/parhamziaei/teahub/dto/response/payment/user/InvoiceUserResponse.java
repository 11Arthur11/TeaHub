package dev.parhamziaei.teahub.dto.response.payment.user;

import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceUserResponse {

    private String invoiceToken;

    private Money money;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    private InvoiceStatus status;

    private Integer taxPercentage;

}
