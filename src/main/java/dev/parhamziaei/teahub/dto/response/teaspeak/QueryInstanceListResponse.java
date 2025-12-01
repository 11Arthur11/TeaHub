package dev.parhamziaei.teahub.dto.response.teaspeak;

import lombok.Data;

@Data
public class QueryInstanceListResponse {

    private Long id;

    private String name;

    private boolean enabled;

    private String status;

    private Integer maxTeaSpeakInstance;

    private Integer usedInstanceSlot;

    private Integer startPort;

    private Integer stopPort;

}
