package dev.parhamziaei.teahub.dto.response.resource;

import dev.parhamziaei.teahub.entity.jpa.resource.BaseResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.sql.In;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakResourceDetailResponse extends BaseResourceDetailResponse {

    private Integer maxClients;
    private Integer port;
    private String privilegeToken;

}
