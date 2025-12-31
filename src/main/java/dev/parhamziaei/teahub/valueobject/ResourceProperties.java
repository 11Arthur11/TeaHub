package dev.parhamziaei.teahub.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Embeddable
@Getter
@Setter
public class ResourceProperties {

    private Duration resourceDeleteTimeAfterSuspend = Duration.ofHours(6);

}
