package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.configuration.properties.ApplicationSettingProperties;
import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.dto.response.payment.user.GatewayListUserResponse;
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
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final GatewayService gatewayService;
    private final CurrentUser currentUser;
    private final PaymentServiceProperties paymentServiceProperties;

    @Operation(
            summary = "Get Gateways",
            description = "Returns a list of all available gateways to choose on payment",
            tags = {"Payment"}
    )
    @GetMapping("/gateways")
    public ResponseEntity<DataResponse<List<GatewayListUserResponse>>> getAllGateways() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getAllGateways(GatewayListUserResponse.class),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "PaymentTransaction Page Redirect",
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
                new RedirectResponse(
                        paymentService.createPaymentGatewayUri(
                            currentUser.getId(),
                            invoiceToken,
                            gatewayId
                        )
                ),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Callback of Aqaye Pardakht",
            description = "Send the callback of aqaye pardakht gateway to this api to verify the payment",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
                    )
            )
    )
    @RequestMapping(
            value = "/gateway/callback/ap",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            method = RequestMethod.POST,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<Void> aqayePardakhtCallback(
            @ParameterObject
            @ModelAttribute APCallbackRequest callbackRequest
    ) {
        paymentService.verifyAPCallback(callbackRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(paymentServiceProperties.paymentSuccessRedirectUri().replace("{invoice_id}",  callbackRequest.getInvoiceId())));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
