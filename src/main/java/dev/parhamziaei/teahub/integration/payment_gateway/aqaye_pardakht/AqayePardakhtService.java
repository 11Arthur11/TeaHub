package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht;

import dev.parhamziaei.teahub.configuration.properties.ApplicationSettingProperties;
import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.payment.PaymentFailedException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APTransactionRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APVerifyRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response.APTransactionResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response.APVerifyResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.GatewayRepository;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class AqayePardakhtService implements PaymentGatewayHandler {

    private final InvoiceRepository invoiceRepo;
    private final GatewayRepository gatewayRepo;
    private final RestClient restClient;
    private String apPinCode;
    private final static String AP_PAYMENT_URL = "https://panel.aqayepardakht.ir/startpay/sandbox/";
    private final String callbackUrl;
    private final PaymentServiceProperties paymentProperties;

    public AqayePardakhtService(
            PaymentServiceProperties paymentProperties,
            GatewayRepository gatewayRepo,
            ApplicationSettingProperties appSetting,
            InvoiceRepository invoiceRepo
    ) {
        this.paymentProperties = paymentProperties;
        this.invoiceRepo = invoiceRepo;
        this.gatewayRepo = gatewayRepo;
        this.callbackUrl = appSetting.backendDomain() + "/v1/payments/gateway/callback/ap"; //appSetting.frontendDomain() + "/payments/gateway/callback?gatewayType=" + PaymentGatewayType.AQAYE_PARDAKHT.name();
        this.restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl("https://panel.aqayepardakht.ir/api/v2")
                .build();
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        try {
            Gateway gateway = gatewayRepo.findByGatewayType(PaymentGatewayType.AQAYE_PARDAKHT)
                    .orElseThrow(GatewayNotFoundException::new);
            this.apPinCode = gateway.getMerchantId();
        } catch (GatewayNotFoundException e) {
            log.warn("Payment-Gateway -> Aqaye pardakht gateway not configured.");
        }
    }

    @Override
    public boolean testGateway() {
        APTransactionRequest request = APTransactionRequest.builder()
                .pin(apPinCode)
                .amount("10000")
                .callback("callbackUrl")
                .invoice_id("test")
                .build();

        APTransactionResponse response = sendCreateRequest(request);
        return response.getStatus().equals("success");
    }

    private APTransactionResponse sendCreateRequest(APTransactionRequest request) {
        try {
            ResponseEntity<APTransactionResponse> response = restClient.post()
                    .uri("/create")
                    .body(request)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(APTransactionResponse.class);

            log.info("Retrieved Aqaye Pardakht transaction response: {}", response.getStatusCode());
            if (response.getBody() != null) {
                return response.getBody();
            }
            throw new GatewayException("gateway verify response is null");
        } catch (RestClientException e) {
            log.error("API call failed for aqaye pardakht transaction create, because: {}", e.getMessage());
            throw new GatewayException("API call failed for aqaye pardakht transaction create");
        }

    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return PaymentGatewayType.AQAYE_PARDAKHT;
    }

    @Override
    public String createPaymentGateway(Invoice invoice) {
        int invoiceAmount = invoice.getMoney().getAmount().intValue();
        int finalAmount = (invoiceAmount + invoiceAmount * (paymentProperties.taxPercentage() / 100));

        APTransactionRequest request = APTransactionRequest.builder()
                .pin(apPinCode)
                .amount(String.valueOf(finalAmount))
                .callback(callbackUrl)
                .invoice_id(invoice.getInvoiceToken())
                .build();

        APTransactionResponse response = sendCreateRequest(request);
        if (response.getStatus().equals("success")) {
            return AP_PAYMENT_URL + response.getTransid();
        }
        throw new GatewayException("Payment Gateway creation failure");
    }

    @Override
    public <T extends CallbackRequest> boolean verifyTransaction(T callbackRequest) {
        if (
                callbackRequest == null
                || !callbackRequest.getStatus().equals("1")
        ) {
            throw new PaymentFailedException("payment failed on gateway side");
        }
        log.debug("retrieved callback: {} - {} - {}", callbackRequest.getTransid(), callbackRequest.getStatus(), callbackRequest.getInvoiceId());

        Invoice invoice = invoiceRepo.findOne(
                Specification.allOf(
                        InvoiceSpecification.hasInvoiceToken(callbackRequest.getInvoiceId())
                )
        ).orElseThrow(NoSuchEntityException::new);

        APVerifyRequest verifyRequest = APVerifyRequest.builder()
                .pin(apPinCode)
                .transid(callbackRequest.getTransid())
                .amount(invoice.getMoney().getAmount().intValue())
                .build();

        APVerifyResponse responseEntity = new APVerifyResponse();
        try {
            ResponseEntity<APVerifyResponse> verifyResponse = restClient.post()
                    .uri("/verify")
                    .body(verifyRequest)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(APVerifyResponse.class);

            if (verifyResponse.getBody() != null) {
                responseEntity = verifyResponse.getBody();
            }

            log.info("Retrieved Aqaye Pardakht verify response: {}", verifyResponse.getStatusCode());
        } catch (RestClientException e) {
            log.error("API call failed for aqaye pardakht verify, because: {}", e.getMessage());
            throw new GatewayException("API call failed for aqaye pardakht verify");
        }

        if (responseEntity != null) {
            return responseEntity.getCode().equals("1") && responseEntity.getStatus().equals("success");
        }
        throw new GatewayException("gateway verify response is null");
    }

}
