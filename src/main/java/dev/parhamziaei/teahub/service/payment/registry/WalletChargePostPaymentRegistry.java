package dev.parhamziaei.teahub.service.payment.registry;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletChargePostPaymentRegistry implements PostPaymentRegistryHandler {

    private final WalletService walletService;

    @Override
    public void processAction(Invoice invoice) {
        walletService.credit(
                invoice.getOwner().getId(),
                invoice.getMoney().getAmount(),
                TransactionReason.WALLET_CHARGE
        );
    }

    @Override
    public PostPaymentType getType() {
        return PostPaymentType.WALLET_CHARGE;
    }

}
