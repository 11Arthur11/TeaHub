package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceFilterRequest extends AbstractPaginationRequest {

    @EnumValue(enumClass = InvoiceStatus.class)
    private InvoiceStatus status;

}
