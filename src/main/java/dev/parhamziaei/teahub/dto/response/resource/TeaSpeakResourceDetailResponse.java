package dev.parhamziaei.teahub.dto.response.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakResourceDetailResponse extends BaseResourceDetailResponse {

    private Integer maxClients;
    private Integer port;
    private String privilegeToken;

}
