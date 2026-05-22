package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht;

import dev.parhamziaei.teahub.configuration.properties.ApplicationSettingProperties;
import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.dto.request.payment.admin.AqayePardakhtPersistRequest;
import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayPersistRequest;
import dev.parhamziaei.teahub.entity.jpa.payment.AqayePardakhtGateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.payment.PaymentFailedException;
import dev.parhamziaei.teahub.exception.custom.service.payment.PaymentVerificationException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APTransactionRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APVerifyRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response.APTransactionResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response.APVerifyResponse;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.AqayePardakhtGatewayRepository;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import dev.parhamziaei.teahub.service.mapper.AqayePardakhtGatewayMapStruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class AqayePardakhtService implements PaymentGatewayHandler {

    private final InvoiceRepository invoiceRepo;
    private final RestClient restClient;
    private final AqayePardakhtGatewayRepository apRepo;
    private final AqayePardakhtGatewayMapStruct mapStruct;
    private String apPinCode;
    private final static String AP_PAYMENT_URL = "https://panel.aqayepardakht.ir/startpay/sandbox/";
    private final String callbackUrl;
    private final PaymentServiceProperties paymentProperties;

    public AqayePardakhtService(
            PaymentServiceProperties paymentProperties,
            ApplicationSettingProperties appSetting,
            InvoiceRepository invoiceRepo,
            AqayePardakhtGatewayRepository apRepo,
            AqayePardakhtGatewayMapStruct mapStruct
    ) {
        this.paymentProperties = paymentProperties;
        this.invoiceRepo = invoiceRepo;
        this.callbackUrl = appSetting.backendDomain() + "/v1/payments/gateway/callback/ap"; //appSetting.frontendDomain() + "/payments/gateway/callback?gatewayType=" + PaymentGatewayType.AQAYE_PARDAKHT.name();
        this.restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl("https://panel.aqayepardakht.ir/api/v2")
                .build();
        this.apRepo = apRepo;
        this.mapStruct = mapStruct;
    }

    @Override
    public <T extends GatewayPersistRequest> void persistGateway(T persistRequest) {
        AqayePardakhtPersistRequest request = (AqayePardakhtPersistRequest) persistRequest;
        AqayePardakhtGateway gateway = apRepo.find()
                .orElse(mapStruct.toEntity(request, new AqayePardakhtGateway()));
        apPinCode = gateway.getMerchantId();
        apRepo.save(gateway);
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
        throw new GatewayException("PaymentTransaction Gateway creation failure");
    }

    @Override
    public <T extends CallbackRequest> void verifyTransaction(T callbackRequest) {
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

        try {
            ResponseEntity<APVerifyResponse> verifyResponse = restClient.post()
                    .uri("/verify")
                    .body(verifyRequest)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(APVerifyResponse.class);

            log.info("Retrieved Aqaye Pardakht verify response: {}", verifyResponse.getStatusCode());
        } catch (HttpClientErrorException.UnprocessableEntity e) {
            log.error("Aqaye Pardakht gateway rejected the transaction: {}", e.getMessage());
            throw new PaymentVerificationException("transaction verification failed");
        } catch (RestClientException e) {
            throw new GatewayException("API call failed for aqaye pardakht verify request");
        }
    }

}
