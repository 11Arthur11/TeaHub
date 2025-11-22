package dev.parhamziaei.teahub.valueobject;

import dev.parhamziaei.teahub.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Money {

    private BigDecimal amount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Currency currency;

}
