package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayConfigRequest;
import dev.parhamziaei.teahub.dto.request.payment.user.GatewayListResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayConfigException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.GatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final GatewayRepository gatewayRepo;
    private final PaymentGatewayFactory gatewayFactory;

    @PreAuthorize("hasRole('ADMIN')")
    public Gateway getGatewayDetail(Long id) {
        return gatewayRepo.findById(id)
                .orElseThrow(GatewayNotFoundException::new);
    }

    public List<PaymentGatewayType> getGatewayTypes() {
        return Arrays.stream(PaymentGatewayType.values()).toList();
    }

    public List<GatewayListResponse> getAllGateways() {
        return gatewayRepo.findAll()
                .stream()
                .map(g -> new GatewayListResponse(g.getId(), g.getName()))
                .toList();
    }

    public void saveGatewayConfig(GatewayConfigRequest configRequest) {
        Gateway gateway = new Gateway(configRequest.getName(), configRequest.getMerchantId(), PaymentGatewayType.AQAYE_PARDAKHT);
        gatewayRepo.save(gateway);
        PaymentGatewayHandler handler = gatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT);

        handler.initialize();
        if (!handler.testGateway()) {
            throw new GatewayConfigException("Gateway config failed maybe merchantId is wrong!");
        }
    }

}
