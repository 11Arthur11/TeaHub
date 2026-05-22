package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
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
public class InvoiceFilterRequest extends BasePaginationRequest {

    @EnumValue(enumClass = InvoiceStatus.class)
    private InvoiceStatus status;
    @Schema(nullable = true)
    private LocalDateTime fromCreatedAt;
    @Schema(nullable = true)
    private LocalDateTime toCreatedAt;

}
