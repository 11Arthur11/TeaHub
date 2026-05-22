package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayPersistRequest;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayConfigException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.GatewayRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final GatewayRepository gatewayRepo;
    private final PaymentGatewayFactory gatewayFactory;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public Gateway getGatewayDetail(Long id) {
        return gatewayRepo.findById(id)
                .orElseThrow(GatewayNotFoundException::new);
    }

    public List<PaymentGatewayType> getGatewayTypes() {
        return Arrays.stream(PaymentGatewayType.values()).toList();
    }

    public <T> List<T> getAllGateways(Class<T> dtoClass) {
        List<T> gateways = gatewayRepo.findAll()
                .stream()
                .map(g -> modelMapper.map(g, dtoClass))
                .toList();

        if (gateways.isEmpty())
            throw new GatewayNotFoundException();

        return gateways;
    }

    public void persistGateway(GatewayPersistRequest persistRequest) {
        gatewayFactory.getGateway(persistRequest.getType())
                .persistGateway(persistRequest);
    }

}
