package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Invoice> hasInvoiceToken(String invoicetoken) {
        return (root, query, cb) -> {
            if (invoicetoken == null) return null;
            return cb.equal(root.get("invoiceToken"), invoicetoken);
        };
    }

    public static Specification<Invoice> mustHaveOwnerId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), userId);
    }

}
