package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;

public class TelnetSessionLoginFailedEvent extends TelnetSessionUnreachableEvent {
    public TelnetSessionLoginFailedEvent(ServerQueryCredentials sessionCredentials) {
        super(sessionCredentials);
    }
}
