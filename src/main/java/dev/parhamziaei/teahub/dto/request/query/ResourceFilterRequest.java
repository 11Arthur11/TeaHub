package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.enums.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceFilterRequest extends AbstractPaginationRequest{

    private ResourceStatus byResourceStatus;
    private ResourceType byType;
    private String byOwnerPhone;

}
