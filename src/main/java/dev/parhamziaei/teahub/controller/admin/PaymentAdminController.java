package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayConfigRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.GatewayService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/payments")
@RequiredArgsConstructor
public class PaymentAdminController {

    private final PaymentService paymentService;
    private final GatewayService gatewayService;
    private final MessageService messageService;

    @GetMapping("/gateways/modules")
    public ResponseEntity<?> getModules() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getGatewayTypes(),
                HttpStatus.OK
        );
    }

    @GetMapping("/gateways/{id}")
    public ResponseEntity<?> getGatewayDetails(@PathVariable Long id) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                gatewayService.getGatewayDetail(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/gateways/init")
    public ResponseEntity<SimpleResponse> addGatewayConfig(@RequestBody GatewayConfigRequest configRequest) {
        gatewayService.saveGatewayConfig(configRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PAYMENT_GATEWAY_CREATED),
                HttpStatus.OK
        );
    }



}
