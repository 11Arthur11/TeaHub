package dev.parhamziaei.teahub.dto.response.teaspeak;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.QueryInstanceStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class QueryInstanceListResponse {

    private Long id;

    private String name;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private QueryInstanceStatus status;

    private Integer maxTeaSpeakInstances;

    private Integer usedInstanceSlot;

    private Integer startPort;

    private Integer stopPort;

}
