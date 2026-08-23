package dev.parhamziaei.teahub.integration;

import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.entity.jpa.user.Role;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.enums.user.Roles;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.RoleRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletTransactionSpecification;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.valueobject.Money;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = WalletServiceIT.JpaSliceConfiguration.class)
@Import({WalletService.class, WalletServiceIT.ModelMapperConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletServiceIT {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("teahub_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired private WalletService walletService;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private WalletTransactionRepository transactionRepository;
    @Autowired private PlatformTransactionManager transactionManager;
    @Autowired private ConfigurableApplicationContext applicationContext;

    @MockitoBean private MessageService messageService;

    @AfterAll
    void closeApplicationContextBeforeContainer() {
        applicationContext.close();
    }

    @Test
    void debitAndCreditPersistBalanceAndAuditTransactions() {
        User user = persistUser("9000000001", new BigDecimal("500"));
        Long walletId = user.getWallet().getId();

        walletService.debit(walletId, new BigDecimal("125"), TransactionReason.PURCHASE, 77L);
        walletService.credit(user.getId(), new BigDecimal("25"), TransactionReason.REFUND);

        Wallet stored = walletRepository.findById(walletId).orElseThrow();
        List<WalletTransaction> transactions = transactionRepository.findAll(
                WalletTransactionSpecification.forWallet(walletId)
        );
        assertEquals(0, new BigDecimal("400.00").compareTo(stored.getBalance().getAmount()));
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().anyMatch(t -> t.getType() == TransactionType.DEBIT));
        assertTrue(transactions.stream().anyMatch(t -> t.getType() == TransactionType.CREDIT));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void concurrentDebitsCannotOverspendWallet() throws Exception {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        User user = transaction.execute(status -> persistUser("9000000002", new BigDecimal("100")));
        assertNotNull(user);
        Long walletId = user.getWallet().getId();
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> first = executor.submit(() -> debitAfter(start, walletId));
            Future<Boolean> second = executor.submit(() -> debitAfter(start, walletId));
            start.countDown();

            long successfulDebits = List.of(first.get(), second.get()).stream().filter(Boolean::booleanValue).count();
            assertEquals(1, successfulDebits);
        }

        Wallet stored = walletRepository.findById(walletId).orElseThrow();
        assertEquals(0, new BigDecimal("20.00").compareTo(stored.getBalance().getAmount()));
        assertEquals(1, transactionRepository.count(WalletTransactionSpecification.forWallet(walletId)));
    }

    private boolean debitAfter(CountDownLatch start, Long walletId) throws InterruptedException {
        start.await();
        try {
            walletService.debit(walletId, new BigDecimal("80"), TransactionReason.PURCHASE, 1L);
            return true;
        } catch (InsufficientBalanceException expected) {
            return false;
        }
    }

    private User persistUser(String phone, BigDecimal balance) {
        Role role = roleRepository.findByName(Roles.ROLE_USER.value())
                .orElseGet(() -> roleRepository.save(new Role(Roles.ROLE_USER.value(), Roles.ROLE_USER.hierarchy())));
        User user = User.builder()
                .phone(phone)
                .email(phone + "@test.invalid")
                .firstName("Integration")
                .lastName("User")
                .enabled(true)
                .build();
        user.setRole(role);
        Wallet wallet = new Wallet();
        wallet.setBalance(new Money(balance));
        user.setWallet(wallet);
        return userRepository.saveAndFlush(user);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackages = "dev.parhamziaei.teahub.entity.jpa")
    @EnableJpaRepositories(basePackages = "dev.parhamziaei.teahub.repository.jpa")
    static class JpaSliceConfiguration {
    }

    @TestConfiguration
    static class ModelMapperConfiguration {
        @Bean
        ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }
}
