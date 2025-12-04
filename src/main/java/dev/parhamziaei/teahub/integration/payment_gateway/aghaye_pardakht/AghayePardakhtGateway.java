package dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht;

import dev.parhamziaei.teahub.configuration.properties.IPPanelProperties;
import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.user.Invoice;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht.dto.AghayePardakhtTransactionRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AghayePardakhtGateway implements PaymentGatewayHandler {

    private final PaymentServiceProperties paymentProperties;
    private final RestClient restClient;

    public AghayePardakhtGateway(PaymentServiceProperties paymentProperties) {
        this.paymentProperties = paymentProperties;
        this.restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl("https://panel.aqayepardakht.ir/api/v2")
                .build();
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return PaymentGatewayType.AGHAYE_PARDAKHT;
    }

    @Override
    public String createTransaction(Invoice invoice) {

        AghayePardakhtTransactionRequest request = AghayePardakhtTransactionRequest.builder()
                .pin(paymentProperties.aghayePardakhtPinCode())
                .amount(invoice.getAmount())
                .invoice_id()
                .build();

    }

}
