package dev.parhamziaei.teahub.dto.response.teaspeak.admin;

import dev.parhamziaei.teahub.enums.teaspeak.QueryInstanceStatus;
import lombok.Data;

@Data
public class QueryInstanceListResponse {

    private Long id;

    private String name;

    private QueryInstanceStatus status;

    private Integer maxTeaSpeakInstance;

    private Integer usedInstanceSlot;

    private Integer startPort;

    private Integer stopPort;

    private boolean active;

}
