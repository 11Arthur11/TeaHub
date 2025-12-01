package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TelnetSessionUnreachableEvent extends AbstractTelnetEvent {
    public TelnetSessionUnreachableEvent(ServerQueryCredentials credentials) {
        super(credentials);
    }
}

