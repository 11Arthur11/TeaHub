package dev.parhamziaei.teahub.dto.response.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class ResourceListAdminResponse extends ResourceListResponse {

    private Long ownerId;

}
