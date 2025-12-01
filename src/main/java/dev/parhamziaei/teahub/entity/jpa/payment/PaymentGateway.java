package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "payment_gateway")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "gateway")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGateway extends BaseEntity<Long> {

    private String name;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private PaymentGatewayType gatewayType;

}
