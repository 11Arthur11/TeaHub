package dev.parhamziaei.teahub.valueobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;

@Embeddable
@Getter
@Setter
public class InvoiceProperties {

    private Long minimumWalletChargeAmountIrt = 100000L;
    private Integer taxPercentage = 9;

    @Transient
    @JsonIgnore
    public BigDecimal getMinimumWalletChargeBigDecimal() {
        return BigDecimal.valueOf(this.minimumWalletChargeAmountIrt);
    }

}
