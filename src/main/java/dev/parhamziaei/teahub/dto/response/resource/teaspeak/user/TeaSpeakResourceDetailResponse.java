package dev.parhamziaei.teahub.dto.response.resource.teaspeak.user;

import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.TeaSpeakResourceTokenResponse;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakResourceDetailResponse extends AbstractResourceDetailResponse {

    private Integer maxClients;
    private Integer port;
    private TeaSpeakStatus teaSpeakStatus;
    private TeaSpeakResourceTokenResponse privilegeToken;
    private String address;

}
