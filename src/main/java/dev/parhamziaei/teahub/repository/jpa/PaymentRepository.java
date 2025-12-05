package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.Payment;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface PaymentRepository extends JpaRepositoryImplementation<Payment, Long> {
}
