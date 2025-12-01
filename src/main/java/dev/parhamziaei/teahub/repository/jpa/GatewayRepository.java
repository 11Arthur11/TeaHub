package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.PaymentGateway;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends JpaRepository<PaymentGateway, Long> {
    Optional<PaymentGateway> findByGatewayType(PaymentGatewayType gatewayType);
}
