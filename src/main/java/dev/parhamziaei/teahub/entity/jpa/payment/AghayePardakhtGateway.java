package dev.parhamziaei.teahub.entity.jpa.payment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("AGHAYE_PARDAKHT")
@Getter
@NoArgsConstructor
public class AghayePardakhtGateway extends PaymentGateway {

    private String gatewayPin;

}
