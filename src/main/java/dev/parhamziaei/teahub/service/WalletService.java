package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletSpecification;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static dev.parhamziaei.teahub.enums.payment.TransactionType.CREDIT;
import static dev.parhamziaei.teahub.enums.payment.TransactionType.DEBIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;

    public void assertSufficientBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);
        if (wallet.getBalance().getAmount().compareTo(amount) < 0)
            throw new InsufficientBalanceException();
    }

    public BigDecimal getBalance(Long userId) {
        return walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new)
                .getBalance().getAmount();
    }

    @Transactional
    public void debit(Long walletId, BigDecimal amount, TransactionReason reason, Long relatedResourceId) {
        Wallet wallet = walletRepo.findByIdAndLock(walletId);

        if (wallet.getOwner().isAdmin()) {
            log.info(
                    "Admin ({}) with phone number ({}) performed a ({}) amount debit operation with no charge",
                    wallet.getOwner().getFullName(),
                    wallet.getOwner().getPhone(),
                    amount
            );
            return;
        }

        if (wallet.getBalance().getAmount().compareTo(amount) < 0)
            throw new InsufficientBalanceException();

        Money newBalance = new Money(wallet.getBalance().getAmount().subtract(amount));
        wallet.setBalance(newBalance);

        WalletTransaction transaction = WalletTransaction.builder()
                .relatedResourceId(relatedResourceId)
                .createdAt(LocalDateTime.now().withNano(0))
                .reason(reason)
                .type(DEBIT)
                .amount(new Money(amount))
                .build();

        wallet.addTransaction(transaction);
    }

    @Transactional
    public void credit(Long userId, BigDecimal amount, TransactionReason reason) {
        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);

        Money newBalance = new Money(wallet.getBalance().getAmount().add(amount));
        wallet.setBalance(newBalance);

        WalletTransaction transaction = WalletTransaction.builder()
                .createdAt(LocalDateTime.now().withNano(0))
                .reason(reason)
                .type(CREDIT)
                .amount(new Money(amount))
                .build();

        wallet.addTransaction(transaction);
    }

}
