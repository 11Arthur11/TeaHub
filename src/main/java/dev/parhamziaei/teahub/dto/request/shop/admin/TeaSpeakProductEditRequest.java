package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.dto.request.RequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@RequestSubType(ResourceType.TEASPEAK_RESOURCE)
public class TeaSpeakProductEditRequest extends AbstractProductEditRequest {
    private Integer maxClients;
}
