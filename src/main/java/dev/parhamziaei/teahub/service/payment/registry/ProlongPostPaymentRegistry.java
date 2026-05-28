package dev.parhamziaei.teahub.service.payment.registry;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.ProlongPostPayment;
import dev.parhamziaei.teahub.enums.payment.PostPaymentType;
import dev.parhamziaei.teahub.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProlongPostPaymentRegistry implements PostPaymentRegistryHandler {

    private final ResourceService resourceService;

    @Override
    public void processAction(Invoice invoice) {
        ProlongPostPayment postPayment = (ProlongPostPayment) invoice.getPostPaymentAction();
        resourceService.prolongByInvoicePaid(postPayment.getResourceId());
    }

    @Override
    public PostPaymentType getType() {
        return PostPaymentType.PROLONG;
    }

}
