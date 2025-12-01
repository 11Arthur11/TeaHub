package dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht;

import dev.parhamziaei.teahub.configuration.properties.ApplicationSettingProperties;
import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.payment.AghayePardakhtGateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht.dto.AghayePardakhtTransactionRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht.dto.AghayePardakhtTransactionResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.TransactionGatewayResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.AghayePardakhtGatewayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class AghayePardakhtService implements PaymentGatewayHandler {

    private final PaymentServiceProperties paymentProperties;
    private final AghayePardakhtGatewayRepository aghayePardakhtGatewayRepo;
    private final RestClient restClient;
    private String aghayePardakhPinCode;
    private static String AGHAYE_PARDAKHT_PAYMENT_URL = "https://panel.aqayepardakht.ir/startpay/sandbox/";
    private String callbackUrl;

    public AghayePardakhtService(
            PaymentServiceProperties paymentProperties,
            AghayePardakhtGatewayRepository aghayePardakhtGatewayRepo,
            ApplicationSettingProperties appSetting
    ) {
        this.paymentProperties = paymentProperties;
        this.aghayePardakhtGatewayRepo = aghayePardakhtGatewayRepo;
        this.callbackUrl = appSetting.domain() + "/integration/aghaye-pardakht/callback";
        this.restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl("https://panel.aqayepardakht.ir/api/v2")
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        try {
            AghayePardakhtGateway gateway = aghayePardakhtGatewayRepo.findByGatewayType(PaymentGatewayType.AGHAYE_PARDAKHT)
                    .orElseThrow(GatewayNotFoundException::new);
            this.aghayePardakhPinCode = gateway.getGatewayPin();
        } catch (GatewayNotFoundException e) {
            log.warn("Payment-Gateway -> Aghaye pardakht gateway not configured.");
        }
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return PaymentGatewayType.AGHAYE_PARDAKHT;
    }

    @Override
    public TransactionGatewayResponse createTransaction(Invoice invoice) {

        AghayePardakhtTransactionRequest request = AghayePardakhtTransactionRequest.builder()
                .pin(aghayePardakhPinCode)
                .amount(String.valueOf(invoice.getMoney().getAmount().intValue()))
                .callback(callbackUrl)
                .invoice_id(invoice.getInvoiceToken())
                .build();

        try {
            ResponseEntity<AghayePardakhtTransactionResponse> response = restClient.post()
                    .uri("/create")
                    .body(request)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(AghayePardakhtTransactionResponse.class);

            assert response.getBody() != null;
            log.info("Retrieved Aghaye Pardakht transaction response: {}", response.getStatusCode());
            return new TransactionGatewayResponse(
                    response.getBody().getTransid(),
                    AGHAYE_PARDAKHT_PAYMENT_URL + response.getBody().getTransid()
            );
        } catch (RestClientException e) {
            log.error("API call failed for aghaye pardakht transaction create, because: {}", e.getMessage());
            throw new GatewayException("API call failed for aghaye pardakht transaction create");
        }
    }

}
