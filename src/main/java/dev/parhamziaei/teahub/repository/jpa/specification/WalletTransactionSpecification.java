package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class WalletTransactionSpecification {

    public static Specification<WalletTransaction> forWallet(Long walletId) {
        return (root, query, cb) -> cb.equal(root.get("wallet").get("id"), walletId);
    }

    public static Specification<WalletTransaction> byTransactionType(TransactionType transactionType) {
        return (root, query, cb) -> {
            if (transactionType == null) return null;
            return cb.equal(root.get("type"), transactionType);
        };
    }

    public static Specification<WalletTransaction> byTransactionReason(TransactionReason transactionReason) {
        return (root, query, cb) -> {
            if (transactionReason == null) return null;
            return cb.equal(root.get("reason"), transactionReason);
        };
    }

    public static Specification<WalletTransaction> byRelatedResourceId(Long relatedResourceId) {
        if (relatedResourceId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("relatedResourceId"), relatedResourceId);
    }

    public static Specification<WalletTransaction> betweenTime(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null || to == null) return null;
            return cb.between(root.get("createdAt"), from, to);
        };
    }

}
