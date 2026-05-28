package dev.parhamziaei.teahub.service.payment.registry;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.enums.payment.PostPaymentType;

public interface PostPaymentRegistryHandler {

    void processAction(Invoice invoice);
    PostPaymentType getType();

}
