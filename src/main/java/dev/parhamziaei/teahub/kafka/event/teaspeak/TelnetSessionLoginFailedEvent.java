package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TelnetSessionLoginFailedEvent extends AbstractTelnetEvent {
    public TelnetSessionLoginFailedEvent(ServerQueryCredentials sessionCredentials) {
        super(sessionCredentials);
    }
}
