package dev.parhamziaei.teahub.dto.response.shop.user;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceUserResponse {

    private String invoiceToken;

    private Money money;

    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    private String status;

}
