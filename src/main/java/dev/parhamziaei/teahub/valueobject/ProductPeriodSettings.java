package dev.parhamziaei.teahub.valueobject;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPeriodSettings {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "suspendDeleteAfterSeconds",
                    column = @Column(name = "hourly_suspend_delete_after")
            )
    })
    private PeriodDeleteSetting hourly = new PeriodDeleteSetting(Duration.ofMinutes(15).toMillis());

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "suspendDeleteAfterSeconds",
                    column = @Column(name = "daily_suspend_delete_after")
            )
    })
    private PeriodDeleteSetting daily = new PeriodDeleteSetting(Duration.ofHours(1).toMillis());

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "suspendDeleteAfterSeconds",
                    column = @Column(name = "monthly_suspend_delete_after")
            )
    })
    private PeriodDeleteSetting monthly = new PeriodDeleteSetting(Duration.ofDays(2).toMillis());

}