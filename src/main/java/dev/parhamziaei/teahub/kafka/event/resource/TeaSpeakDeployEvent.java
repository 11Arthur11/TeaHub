package dev.parhamziaei.teahub.kafka.event.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeaSpeakDeployEvent {

    private Integer maxClients;
    private Long baseResourceId;

}
