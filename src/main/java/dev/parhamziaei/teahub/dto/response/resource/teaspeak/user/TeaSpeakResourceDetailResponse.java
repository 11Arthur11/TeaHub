package dev.parhamziaei.teahub.dto.response.resource.teaspeak.user;

import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.TeaSpeakResourceTokenResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakResourceDetailResponse extends BaseResourceDetailResponse {

    private Integer maxClients;
    private Integer port;
    private String teaSpeakStatus;
    private TeaSpeakResourceTokenResponse privilegeToken;

}
