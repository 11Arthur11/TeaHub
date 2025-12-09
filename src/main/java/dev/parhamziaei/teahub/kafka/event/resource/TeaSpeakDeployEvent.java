package dev.parhamziaei.teahub.kafka.event.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeaSpeakDeployEvent implements Serializable {

    private Integer maxClients;
    private Long baseResourceId;

}
