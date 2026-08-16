package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.internal.shop.Renewal;
import dev.parhamziaei.teahub.dto.request.payment.admin.WalletTransactionAdminRequest;
import dev.parhamziaei.teahub.dto.request.query.WalletTransactionFilterRequest;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.user.WalletTransactionResponse;
import dev.parhamziaei.teahub.dto.response.user.user.WalletOverviewResponse;
import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletRepository;
import dev.parhamziaei.teahub.repository.jpa.WalletTransactionRepository;
import dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate;
import dev.parhamziaei.teahub.repository.jpa.specification.BillableResourceSpecification;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletSpecification;
import dev.parhamziaei.teahub.repository.jpa.specification.WalletTransactionSpecification;
import dev.parhamziaei.teahub.utils.PersianPeriod;
import dev.parhamziaei.teahub.valueobject.TimeRange;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static dev.parhamziaei.teahub.enums.payment.TransactionType.CREDIT;
import static dev.parhamziaei.teahub.enums.payment.TransactionType.DEBIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;
    private final WalletTransactionRepository walletTransactionRepo;
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final BillableResourceRepository billableResourceRepository;
    private final UserRepository userRepository;

    public void assertSufficientBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);

        if (wallet.getOwner().isAdmin())
            return;

        if (wallet.getBalance().getAmount().compareTo(amount) < 0)
            throw new InsufficientBalanceException();
    }

    public BigDecimal getBalance(Long userId) {
        return walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new)
                .getBalance().getAmount();
    }

    public WalletOverviewResponse getOverview(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        Wallet wallet = walletRepo.findOne(WalletSpecification.forUserId(userId))
                .orElseThrow(NoSuchEntityException::new);

        Specification<WalletTransaction> spec = WalletTransactionSpecification.forWallet(wallet.getId())
                .and(WalletTransactionSpecification.betweenTime(now.minus(Duration.ofDays(30)), now))
                .and(WalletTransactionSpecification.byTransactionType(DEBIT));

        List<WalletTransaction> transactions = walletTransactionRepo.findAll(spec);

        BigDecimal spentLastMonth = sumTransactions(transactions);

        BigDecimal spentLastWeek = sumTransactions(
                transactions.stream()
                        .filter(t -> t.getCreatedAt().isAfter(now.minusDays(7)))
                        .toList()
        );

        BigDecimal spentLastDay = sumTransactions(
                transactions.stream()
                        .filter(t -> t.getCreatedAt().isAfter(now.minusDays(1)))
                        .toList()
        );

        return WalletOverviewResponse.builder()
                .autoRenewalCoverageUntil(calculateAutoRenewalCoverage(wallet))
                .balance(wallet.getBalance())
                .spentLast30days(new Money(spentLastMonth))
                .spentLast7days(new Money(spentLastWeek))
                .spentLastDay(new Money(spentLastDay))
                .build();
    }

    private BigDecimal sumTransactions(List<WalletTransaction> transactions) {
        return transactions.stream()
                .map(WalletTransaction::getAmount)
                .map(Money::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected LocalDateTime calculateAutoRenewalCoverage(Wallet wallet) {
        Specification<BillableResource> spec = BillableResourceSpecification.byUserId(wallet.getOwner().getId());
        List<Renewal> resources = billableResourceRepository.findAll(spec)
                .stream()
                .filter(BillableResource::isAutoProlong)
                .map(r ->
                        new Renewal(
                                r.getExpiration(),
                                r.getProduct().getPeriod().duration(),
                                r.getProduct().getPrice().getAmount()
                        )
                )
                .toList();

        PriorityQueue<Renewal> queue =
                new PriorityQueue<>(Comparator.comparing(Renewal::expiration));

        queue.addAll(resources);

        BigDecimal currentBalance = wallet.getBalance().getAmount();
        while (true) {
            Renewal renewal = queue.poll();

            if (renewal == null) {
                return null;
            }

            if (currentBalance.compareTo(renewal.price()) < 0) {
                return renewal.expiration();
            }

            currentBalance = currentBalance.subtract(renewal.price());

            queue.add(new Renewal(
                    renewal.expiration().plus(renewal.period()),
                    renewal.period(),
                    renewal.price()
            ));
        }
    }

    public PagedModel<WalletTransactionResponse> getWalletTransactionsByUserId(Long userId, WalletTransactionFilterRequest filter) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchEntityException::new);
        return getWalletTransactions(user.getWallet().getId(), filter);
    }

    @Transactional
    public void transactionByAdmin(Long userId, WalletTransactionAdminRequest request) {
        Wallet wallet = walletRepo.findByOwnerIdAndLock(userId);
        Money newBalance = switch (request.getTransactionType()) {
            case DEBIT ->
                new Money(wallet.getBalance().getAmount().subtract(request.getAmount()));
            case CREDIT ->
                new Money(wallet.getBalance().getAmount().add(request.getAmount()));
        };
        wallet.setBalance(newBalance);

        if (request.isPersist()) {
            WalletTransaction transaction = WalletTransaction.builder()
                    .createdAt(LocalDateTime.now().withNano(0))
                    .reason(request.getTransactionReason())
                    .type(request.getTransactionType())
                    .amount(new Money(request.getAmount()))
                    .build();
            wallet.addTransaction(transaction);
            walletTransactionRepo.save(transaction);
        }

        walletRepo.save(wallet);
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
                    res.setReason(wt.getReason());
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

    public Money totalBalance() {
        return new Money(walletRepo.sumAllWalletBalances());
    }

    public AdminMetric.FinanceFlowComparison totalSpendingComparison() {
        TimeRange today = PersianPeriod.today();
        TimeRange yesterday = PersianPeriod.yesterday();

        TimeRange thisMonth = PersianPeriod.thisMonth();
        TimeRange lastMonth = PersianPeriod.lastMonth();

        TimeRange thisWeek = PersianPeriod.thisWeek();
        TimeRange lastWeek = PersianPeriod.lastWeek();

        FinanceFlowAggregate current = walletTransactionRepo.aggregate(
                DEBIT,

                today.start(),
                today.end(),

                thisWeek.start(),
                thisWeek.end(),

                thisMonth.start(),
                thisMonth.end()
        );

        FinanceFlowAggregate previous = walletTransactionRepo.aggregate(
                DEBIT,

                yesterday.start(),
                yesterday.end(),

                lastWeek.start(),
                lastWeek.end(),

                lastMonth.start(),
                lastMonth.end()
        );


        return new AdminMetric.FinanceFlowComparison(
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.daily().longValue()),
                        BigDecimal.valueOf(previous.daily().longValue())
                ),
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.weekly().longValue()),
                        BigDecimal.valueOf(previous.weekly().longValue())
                ),
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.monthly().longValue()),
                        BigDecimal.valueOf(previous.monthly().longValue())
                )
        );
    }

    @Transactional
    public void debit(Long walletId, BigDecimal amount, TransactionReason reason, Long relatedResourceId) {
        Wallet wallet = walletRepo.findByIdAndLock(walletId);

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
        walletTransactionRepo.save(transaction);
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
        walletTransactionRepo.save(transaction);
    }

}
