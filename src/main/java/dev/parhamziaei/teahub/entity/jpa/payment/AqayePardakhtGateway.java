package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("AQAYE_PARDAKHT")
public class AqayePardakhtGateway extends Gateway {

    public static Long STATIC_ID = 1L;

    private String merchantId;

    public AqayePardakhtGateway() {
        super(PaymentGatewayType.AQAYE_PARDAKHT, STATIC_ID);
    }

}
