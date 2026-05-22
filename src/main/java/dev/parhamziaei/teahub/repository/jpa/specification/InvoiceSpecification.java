package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class InvoiceSpecification {

    public static Specification<Invoice> hasStatus(InvoiceStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Invoice> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return null;
            return cb.equal(root.get("owner").get("id"), userId);
        };
    }

    public static Specification<Invoice> hasInvoiceToken(String invoiceToken) {
        return (root, query, cb) -> {
            if (invoiceToken == null) return null;
            return cb.equal(root.get("invoiceToken"), invoiceToken);
        };
    }

    public static Specification<Invoice> mustHaveOwnerId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), userId);
    }

    public static Specification<Invoice> betweenTime(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null || to == null) return null;
            return cb.between(root.get("createdAt"), from, to);
        };
    }

}
