package dev.parhamziaei.teahub.entity.jpa.payment.invoice;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("PROLONG")
@AllArgsConstructor
@NoArgsConstructor
public class ProlongPostPayment extends PostPaymentAction {

    private Long resourceId;

}
