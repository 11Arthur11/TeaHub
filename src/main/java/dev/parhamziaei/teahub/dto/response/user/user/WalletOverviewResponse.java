package dev.parhamziaei.teahub.dto.response.user.user;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletOverviewResponse {

    private Money balance;
    private Money spentLast30days;
    private Money spentLast7days;
    private Money spentLastDay;
    private LocalDateTime autoRenewalCoverageUntil;

}
