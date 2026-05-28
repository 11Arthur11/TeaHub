package dev.parhamziaei.teahub.entity.jpa.payment.invoice;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DiscriminatorValue("WALLET_CHARGE")
public class WalletChargePostPayment extends PostPaymentAction {
}
