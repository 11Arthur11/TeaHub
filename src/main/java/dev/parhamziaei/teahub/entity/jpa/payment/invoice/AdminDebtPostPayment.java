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
@DiscriminatorValue("ADMIN_DEBT")
@AllArgsConstructor
@NoArgsConstructor
public class AdminDebtPostPayment extends PostPaymentAction {

    private Long adminId;

}
