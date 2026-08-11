package dev.parhamziaei.teahub.dto.request.system;

import dev.parhamziaei.teahub.valueobject.InvoiceProperties;
import dev.parhamziaei.teahub.valueobject.ProductPeriodSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSettingDto {

    private InvoiceProperties invoiceProperties;

    private ProductPeriodSettings productPeriodSettings;

}
