package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.InvoiceStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceFilterRequest extends AbstractPaginationRequest {

    @EnumValue(enumClass = InvoiceStatus.class)
    private InvoiceStatus status;
}
