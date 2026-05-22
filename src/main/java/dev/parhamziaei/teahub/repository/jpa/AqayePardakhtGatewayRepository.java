package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.AqayePardakhtGateway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AqayePardakhtGatewayRepository extends JpaRepository<AqayePardakhtGateway, Long> {

    default Optional<AqayePardakhtGateway> find() {
        return findById(AqayePardakhtGateway.STATIC_ID);
    }

}
