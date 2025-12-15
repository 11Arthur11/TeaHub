package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    Optional<Gateway> findByGatewayType(PaymentGatewayType gatewayType);
}
