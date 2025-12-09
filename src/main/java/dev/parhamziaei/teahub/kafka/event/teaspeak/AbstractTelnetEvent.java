package dev.parhamziaei.teahub.kafka.event.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AbstractTelnetEvent implements Serializable {

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
