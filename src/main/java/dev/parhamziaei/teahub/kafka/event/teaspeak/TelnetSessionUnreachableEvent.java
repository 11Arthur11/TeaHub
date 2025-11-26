package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.TelnetSession;
import lombok.Data;

@Data
public class TelnetSessionUnreachableEvent {

    private ServerQueryCredentials sessionCredentials;
    private String ip;
    private Integer port;

    public TelnetSessionUnreachableEvent(ServerQueryCredentials sessionCredentials) {
        this.sessionCredentials = sessionCredentials;
        this.ip = sessionCredentials.ip();
        this.port = sessionCredentials.port();
    }

}
