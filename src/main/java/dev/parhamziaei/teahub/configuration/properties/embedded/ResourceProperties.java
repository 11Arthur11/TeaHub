package dev.parhamziaei.teahub.configuration.properties.embedded;

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
