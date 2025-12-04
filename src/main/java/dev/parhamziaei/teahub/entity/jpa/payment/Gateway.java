package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Gateway extends BaseEntity<Long> {

    private String name;

    private String merchantId;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private PaymentGatewayType gatewayType;

}
