package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletSpecification;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;

    public BigDecimal getBalance(Long userId) {
        return walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new)
                .getBalance().getAmount();
    }

    @Transactional
    public void debit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);

        if (wallet.getBalance().getAmount().compareTo(amount) < 0)
            throw new InsufficientBalanceException();

        Money newBalance = new Money(wallet.getBalance().getAmount().subtract(amount));
        wallet.setBalance(newBalance);
    }

    @Transactional
    public void credit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);

        Money newBalance = new Money(wallet.getBalance().getAmount().add(amount));
        wallet.setBalance(newBalance);
    }

}
