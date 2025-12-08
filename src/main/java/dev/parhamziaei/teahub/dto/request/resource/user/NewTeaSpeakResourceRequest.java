package dev.parhamziaei.teahub.dto.request.resource.user;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.enums.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewTeaSpeakResourceRequest extends AbstractNewResourceRequest {
    @Override
    public ResourceType getResourceType() {
        return ResourceType.TEASPEAK;
    }
}
