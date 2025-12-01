package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import lombok.Data;

@Data
public abstract class AbstractTelnetEvent {

    private ServerQueryCredentials credentials;
    private String ip;
    private Integer port;

    public AbstractTelnetEvent(){}

    public AbstractTelnetEvent(ServerQueryCredentials sessionCredentials) {
        this.credentials = sessionCredentials;
        this.ip = sessionCredentials.ip();
        this.port = sessionCredentials.port();
    }

}
