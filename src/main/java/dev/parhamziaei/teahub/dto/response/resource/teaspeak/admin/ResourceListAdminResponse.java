package dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin;

import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.ResourceListResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceListAdminResponse extends ResourceListResponse {

    private String ownerPhone;

}
