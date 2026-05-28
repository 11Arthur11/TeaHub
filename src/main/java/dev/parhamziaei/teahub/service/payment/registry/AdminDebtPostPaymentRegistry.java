package dev.parhamziaei.teahub.service.payment.registry;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import org.springframework.stereotype.Component;

@Component
public class AdminDebtPostPaymentRegistry implements PostPaymentRegistryHandler {

    @Override
    public void processAction(Invoice invoice) {
        // TODO maybe log for admin here!
    }

    @Override
    public PostPaymentType getType() {
        return PostPaymentType.ADMIN_DEBT;
    }

}
