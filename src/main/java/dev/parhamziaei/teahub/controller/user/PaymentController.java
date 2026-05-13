package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.dto.request.payment.user.GatewayListResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.RedirectResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.service.GatewayService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final GatewayService gatewayService;
    private final MessageService messageService;

    @Operation(
            summary = "Get Gateways",
            description = "Returns a list of all available gateways to choose on payment",
            tags = {"Payment"}
    )
    @GetMapping("/gateways")
    public ResponseEntity<DataResponse<List<GatewayListResponse>>> getAllGateways() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getAllGateways(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Payment Page Redirect",
            description = "Returns a redirect response of payment gateway",
            tags = {"Payment"}
    )
    @PostMapping("/pay")
    public ResponseEntity<DataResponse<RedirectResponse>> payInvoice(
            @RequestParam("invoiceToken") String invoiceToken,
            @RequestParam("gatewayId") Long gatewayId
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                new RedirectResponse(paymentService.createPaymentGatewayUri(invoiceToken, gatewayId)),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Callback of Aqaye Pardakht",
            description = "Send the callback of aqaye pardakht gateway to this api to verify the payment",
            tags = {"Payment"}
    )
    @RequestMapping(
            value = "/gateway/callback/ap",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<SimpleResponse> aqayePardakhtCallback(@ModelAttribute APCallbackRequest callbackRequest) {
        paymentService.verifyAPCallback(callbackRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PAYMENT_INVOICE_PAID),
                HttpStatus.OK
        );
    }
}
