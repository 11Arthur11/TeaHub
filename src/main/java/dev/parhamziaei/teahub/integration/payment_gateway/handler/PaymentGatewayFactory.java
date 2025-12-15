package dev.parhamziaei.teahub.integration.payment_gateway.handler;

import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentGatewayFactory {

    private final Map<PaymentGatewayType, PaymentGatewayHandler> gateways;

    public PaymentGatewayFactory(
            List<PaymentGatewayHandler> gateways
    ) {
        this.gateways = gateways.stream()
                .collect(Collectors.toMap(PaymentGatewayHandler::getGatewayType, g -> g));
    }

    public PaymentGatewayHandler getGateway(PaymentGatewayType gatewayType) {
        return gateways.get(gatewayType);
    }
}
