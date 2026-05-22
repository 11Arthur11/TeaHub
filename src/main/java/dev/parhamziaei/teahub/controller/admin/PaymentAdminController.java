package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayPersistRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.payment.admin.APGatewayListAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.service.GatewayService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/payments")
@RequiredArgsConstructor
public class PaymentAdminController {

    private final GatewayService gatewayService;
    private final MessageService messageService;

    @Operation(
            summary = "Get available payment modules",
            description = "Returns a list of available payment modules. " +
                    "Each module in the list is supported by the web application and is ready to be configured.",
            tags = {"PaymentTransaction (Admin)"}
    )
    @GetMapping("/gateways/modules")
    public ResponseEntity<DataResponse<List<PaymentGatewayType>>> getModules() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getGatewayTypes(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get payment gateway details",
            description = "Returns detailed information about the specified payment gateway, including merchant ID and configuration settings.",
            tags = {"PaymentTransaction (Admin)"}
    )
    @GetMapping("/gateways/{gatewayId}")
    public ResponseEntity<DataResponse<Gateway>> getGatewayDetails(@PathVariable Long gatewayId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getGatewayDetail(gatewayId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Initialize or edit payment gateway configuration",
            description = "Creates and initializes a payment gateway configuration so it can be used for processing payments.",
            tags = {"PaymentTransaction (Admin)"}
    )
    @PostMapping("/gateways")
    public ResponseEntity<SimpleResponse> addGatewayConfig(@RequestBody GatewayPersistRequest persistRequest) {
        gatewayService.persistGateway(persistRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PAYMENT_GATEWAY_CREATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get all gateways",
            description = "Get all gateways as list - this response is not paginated",
            tags = {"PaymentTransaction (Admin)"}
    )
    @GetMapping("/gateways")
    public ResponseEntity<DataResponse<List<Gateway>>> getGateways() {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                gatewayService.getAllGateways(Gateway.class),
                HttpStatus.OK
        );
    }


}
