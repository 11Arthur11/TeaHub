package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.AghayePardakhtGateway;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AghayePardakhtGatewayRepository extends JpaRepository<AghayePardakhtGateway, Long> {
    Optional<AghayePardakhtGateway> findByGatewayType(PaymentGatewayType gatewayType);
}
