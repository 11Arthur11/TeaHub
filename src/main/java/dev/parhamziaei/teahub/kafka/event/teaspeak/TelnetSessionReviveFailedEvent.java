package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TelnetSessionReviveFailedEvent extends AbstractTelnetEvent {
    public TelnetSessionReviveFailedEvent(ServerQueryCredentials credentials) {
        super(credentials);
    }
}
