package dev.parhamziaei.teahub.dto.request.shop.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class CreditPackageRequest {

    @Length(min = 5)
    private String packageName;
    private BigDecimal amount;
    private boolean enabled;

}
