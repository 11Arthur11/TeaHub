package dev.parhamziaei.teahub.dto.response.teaspeak.admin;

import dev.parhamziaei.teahub.enums.teaspeak.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstanceListResponse {

    private Long id;

    private String name;

    private QueryInstanceStatus status;

    private ServerQueryCredentials credentials;

    private Integer defaultQueryServerGroupId;

    private Integer maxTeaSpeakInstance;

    private Integer usedInstanceSlot;

    private Integer startPort;

    private Integer stopPort;

    private boolean active;

}
