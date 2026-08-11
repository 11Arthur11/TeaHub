package dev.parhamziaei.teahub.valueobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodDeleteSetting {

    private Long suspendDeleteAfterSeconds;

    @JsonIgnore
    public Duration getSuspendDeleteAfter() {
        return Duration.ofSeconds(suspendDeleteAfterSeconds);
    }
}