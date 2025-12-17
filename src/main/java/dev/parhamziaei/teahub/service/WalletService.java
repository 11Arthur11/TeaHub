package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.dto.response.user.WalletTransactionResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletSpecification;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletTransactionSpecification;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static dev.parhamziaei.teahub.enums.payment.TransactionType.CREDIT;
import static dev.parhamziaei.teahub.enums.payment.TransactionType.DEBIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;
    private final WalletTransactionRepository walletTransactionRepo;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

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

    public PagedModel<WalletTransactionResponse> getWalletTransactions(Long walletId, WalletTransactionFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<WalletTransaction> spec = WalletTransactionSpecification.forWallet(walletId)
                .and(WalletTransactionSpecification.byTransactionType(filter.getTransactionType()))
                .and(WalletTransactionSpecification.byTransactionReason(filter.getTransactionReason()))
                .and(WalletTransactionSpecification.betweenTime(filter.getFromCreatedAt(), filter.getToCreatedAt()))
                .and(WalletTransactionSpecification.byRelatedResourceId(filter.getRelatedResourceId()));

        Page<WalletTransaction> page = walletTransactionRepo.findAll(spec, pageable);

        if (page.getContent().isEmpty())
            throw new NoSuchDataException();

        List<WalletTransactionResponse> mapped = page.getContent()
                .stream()
                .map(wt -> {
                    WalletTransactionResponse res = modelMapper.map(wt, WalletTransactionResponse.class);
                    res.setReason(messageService.get(wt.getReason()));
                    return res;
                })
                .toList();

        return new PagedModel<>(
                new PageImpl<>(
                        mapped,
                        page.getPageable(),
                        page.getTotalElements()
                )
        );
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
