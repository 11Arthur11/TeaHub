package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.support.TestFixtures;
import dev.parhamziaei.teahub.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock private WalletRepository walletRepository;
    @Mock private WalletTransactionRepository transactionRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private MessageService messageService;
    @Mock private BillableResourceRepository resourceRepository;
    @Mock private UserRepository userRepository;

    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletService(
                walletRepository,
                transactionRepository,
                modelMapper,
                messageService,
                resourceRepository,
                userRepository
        );
    }

    @Test
    void rejectsBalanceBelowRequestedAmount() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("99.99"));
        when(walletRepository.findOne(ArgumentMatchers.<Specification<Wallet>>any()))
                .thenReturn(Optional.of(user.getWallet()));

        assertThrows(
                InsufficientBalanceException.class,
                () -> walletService.assertSufficientBalance(user.getId(), new BigDecimal("100.00"))
        );
    }

    @Test
    void adminBalanceIsUnlimited() {
        User admin = TestFixtures.admin(1L, 10L, BigDecimal.ZERO);
        when(walletRepository.findOne(ArgumentMatchers.<Specification<Wallet>>any()))
                .thenReturn(Optional.of(admin.getWallet()));

        assertDoesNotThrow(() -> walletService.assertSufficientBalance(admin.getId(), new BigDecimal("999999")));
    }

    @Test
    void debitLocksWalletUpdatesBalanceAndPersistsAuditTransaction() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("500"));
        when(walletRepository.findByIdAndLock(10L)).thenReturn(user.getWallet());
        ArgumentCaptor<WalletTransaction> transaction = ArgumentCaptor.forClass(WalletTransaction.class);

        walletService.debit(10L, new BigDecimal("125"), TransactionReason.PURCHASE, 77L);

        assertEquals(new BigDecimal("375"), user.getWallet().getBalance().getAmount());
        verify(transactionRepository).save(transaction.capture());
        assertEquals(TransactionType.DEBIT, transaction.getValue().getType());
        assertEquals(TransactionReason.PURCHASE, transaction.getValue().getReason());
        assertEquals(77L, transaction.getValue().getRelatedResourceId());
        assertSame(user.getWallet(), transaction.getValue().getWallet());
    }

    @Test
    void failedDebitDoesNotCreateTransaction() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("10"));
        when(walletRepository.findByIdAndLock(10L)).thenReturn(user.getWallet());

        assertThrows(
                InsufficientBalanceException.class,
                () -> walletService.debit(10L, new BigDecimal("11"), TransactionReason.PURCHASE, 77L)
        );
        assertEquals(new BigDecimal("10"), user.getWallet().getBalance().getAmount());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void creditUpdatesBalanceAndPersistsCreditTransaction() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("25"));
        when(walletRepository.findOne(ArgumentMatchers.<Specification<Wallet>>any()))
                .thenReturn(Optional.of(user.getWallet()));
        ArgumentCaptor<WalletTransaction> transaction = ArgumentCaptor.forClass(WalletTransaction.class);

        walletService.credit(1L, new BigDecimal("75"), TransactionReason.WALLET_CHARGE);

        assertEquals(new BigDecimal("100"), user.getWallet().getBalance().getAmount());
        verify(transactionRepository).save(transaction.capture());
        assertEquals(TransactionType.CREDIT, transaction.getValue().getType());
        assertEquals(new Money(new BigDecimal("75")), transaction.getValue().getAmount());
    }

    @Test
    void missingWalletCannotBeCredited() {
        when(walletRepository.findOne(ArgumentMatchers.<Specification<Wallet>>any())).thenReturn(Optional.empty());

        assertThrows(
                NoSuchEntityException.class,
                () -> walletService.credit(99L, BigDecimal.ONE, TransactionReason.WALLET_CHARGE)
        );
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void calculatesFirstRenewalThatCurrentBalanceCannotCover() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("25"));
        LocalDateTime firstExpiry = LocalDateTime.now().plusDays(1).withNano(0);
        BillableProduct product = TestFixtures.product(1L, new BigDecimal("10"), ProductPeriod.DAILY);
        BillableResource resource = TestFixtures.resource(1L, user, product, ResourceStatus.ACTIVE, firstExpiry);
        when(resourceRepository.findAll(ArgumentMatchers.<Specification<BillableResource>>any()))
                .thenReturn(List.of(resource));

        LocalDateTime coverage = walletService.calculateAutoRenewalCoverage(user.getWallet());

        assertEquals(firstExpiry.plusDays(2), coverage);
    }

    @Test
    void returnsNullCoverageWhenUserHasNoAutoRenewResources() {
        User user = TestFixtures.user(1L, 10L, new BigDecimal("25"));
        when(resourceRepository.findAll(ArgumentMatchers.<Specification<BillableResource>>any())).thenReturn(List.of());

        assertNull(walletService.calculateAutoRenewalCoverage(user.getWallet()));
    }
}
