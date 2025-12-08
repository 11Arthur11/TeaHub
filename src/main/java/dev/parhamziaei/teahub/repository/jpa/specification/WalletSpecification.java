package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import org.springframework.data.jpa.domain.Specification;

public class WalletSpecification {

    public static Specification<Wallet> forUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), userId);
    }
}
