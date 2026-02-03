package dev.parhamziaei.teahub.dto.response.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceListAdminResponse extends ResourceListResponse {

    private String ownerPhone;

}
