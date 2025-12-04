package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.dto.response.global.RedirectResponse;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.service.GatewayService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final GatewayService gatewayService;
    private final MessageService messageService;

    @GetMapping("/gateways")
    public ResponseEntity<?> getAllGateways() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getAllGateways(),
                HttpStatus.OK
        );
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payInvoice(
            @RequestParam("invoiceToken") String invoiceToken,
            @RequestParam("gatewayId") Long gatewayId
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                new RedirectResponse(paymentService.createPaymentGateway(invoiceToken, gatewayId)),
                HttpStatus.CREATED
        );
    }

    @RequestMapping(
            value = "/gateway/callback/ap",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<?> aqayePardakhtCallback(@ModelAttribute APCallbackRequest callbackRequest) {
        paymentService.verifyAPCallback(callbackRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PAYMENT_INVOICE_PAID),
                HttpStatus.OK
        );
    }
}
