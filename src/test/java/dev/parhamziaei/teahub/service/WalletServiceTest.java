package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletTransactionSpecification;
import dev.parhamziaei.teahub.test_util.UserTestUtil;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    private UserTestUtil userTestUtil;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletTransactionRepository transactionRepository;

    @Test
    void assertSufficientBalanceTest_InsufficientBalance() {
        User user = userTestUtil.persistedDummyUser();
        Assertions.assertThrows(
                InsufficientBalanceException.class,
                () -> walletService.assertSufficientBalance(user.getId(), new BigDecimal(500))
        );
    }

    @Test
    @Transactional
    void assertSufficientBalanceTest_sufficientBalance() {
        User user = userTestUtil.persistedDummyUser();
        user.getWallet().setBalance(new Money(new BigDecimal(500)));
        Assertions.assertDoesNotThrow(() -> walletService.assertSufficientBalance(user.getId(), new BigDecimal(499)));
    }

    @Test
    @Transactional
    void debitTest() {
        User user = userTestUtil.persistedDummyUser();
        Wallet wallet = user.getWallet();
        wallet.setBalance(new Money(new BigDecimal(500)));
        Assertions.assertDoesNotThrow( () ->
                walletService.debit(
                        wallet.getId(),
                        new BigDecimal(400),
                        TransactionReason.PURCHASE,
                        1L
                )
        );
        Assertions.assertEquals(new Money(new BigDecimal(100)), wallet.getBalance());
    }

    @Test
    @Transactional
    void debitShouldCreateWalletTransactionTest() {
        User user = userTestUtil.persistedDummyUser();
        Wallet wallet = user.getWallet();
        wallet.setBalance(new Money(new BigDecimal(500)));
        final long userTransactionsBeforeDebit = transactionRepository.count(WalletTransactionSpecification.forWallet(wallet.getId()));
        Assertions.assertDoesNotThrow( () ->
                walletService.debit(
                        wallet.getId(),
                        new BigDecimal(400),
                        TransactionReason.PURCHASE,
                        1L
                )
        );
        Assertions.assertTrue(transactionRepository.count(WalletTransactionSpecification.forWallet(wallet.getId())) > userTransactionsBeforeDebit);
    }

    @Test
    @Transactional
    void creditTest() {
        User user = userTestUtil.persistedDummyUser();
        Assertions.assertDoesNotThrow( () ->
                walletService.credit(
                        user.getId(),
                        new BigDecimal(400),
                        TransactionReason.WALLET_CHARGE
                )
        );
        Assertions.assertDoesNotThrow( () ->
                walletService.assertSufficientBalance(user.getId(), new BigDecimal(400))
        );
    }

    @Test
    @Transactional
    void creditShouldCreateWalletTransactionTest() {
        User user = userTestUtil.persistedDummyUser();
        Wallet wallet = user.getWallet();
        final long userTransactionsBeforeDebit = transactionRepository.count(WalletTransactionSpecification.forWallet(wallet.getId()));
        Assertions.assertDoesNotThrow( () ->
                walletService.credit(
                        user.getId(),
                        new BigDecimal(400),
                        TransactionReason.WALLET_CHARGE
                )
        );
        Assertions.assertTrue(transactionRepository.count(WalletTransactionSpecification.forWallet(wallet.getId())) > userTransactionsBeforeDebit);
    }

}