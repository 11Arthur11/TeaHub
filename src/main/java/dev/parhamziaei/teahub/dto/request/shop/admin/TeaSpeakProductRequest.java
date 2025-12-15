package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.dto.request.RequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@RequestSubType(ResourceType.TEASPEAK_RESOURCE)
public class TeaSpeakProductRequest extends AbstractProductRequest {

    private Integer maxClients;

}
