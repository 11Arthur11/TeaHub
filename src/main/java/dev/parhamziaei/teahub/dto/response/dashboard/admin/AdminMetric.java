package dev.parhamziaei.teahub.dto.response.dashboard.admin;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMetric {

    private FinanceMetric financeMetric;
    private UserMetric userMetric;
    private TicketMetric ticketMetric;
    private ResourceMetric resourceMetric;
    private NodeMetric queryInstanceMetric;
    private NodeMetric audioBotNodeMetric;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserMetric {

        private Long currentOnline;
        private PeriodComparison<Long> dailyRegisters;
        private PeriodComparison<Long> weeklyRegisters;
        private PeriodComparison<Long> monthlyRegisters;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FinanceMetric {

        private Money totalBalance;
        private AdminMetric.FinanceFlowComparison income;
        private AdminMetric.FinanceFlowComparison spending;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PeriodComparison<T> {
        private T current;
        private T previous;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinanceFlowComparison {

        private PeriodComparison<BigDecimal> daily;
        private PeriodComparison<BigDecimal> weekly;
        private PeriodComparison<BigDecimal> monthly;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeMetric {
        private CountSummary nodeSummary;
        private ProvisionStrategy nodeStrategy;
    }

}
