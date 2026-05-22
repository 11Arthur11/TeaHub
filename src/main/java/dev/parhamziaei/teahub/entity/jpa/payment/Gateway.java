package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "gateway")
@DiscriminatorColumn(name = "gateway_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
public class Gateway {

    @Id
    private Long id;

    private String name;

    private boolean active;

    @Column(name = "gateway_type", updatable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGatewayType type;

    public Gateway(PaymentGatewayType type, Long id) {
        this.id = id;
        this.type = type;
    }

}
