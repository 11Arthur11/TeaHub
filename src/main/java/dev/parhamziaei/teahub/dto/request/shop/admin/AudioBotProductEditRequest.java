package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.dto.request.ResourceRequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ResourceRequestSubType(ResourceType.AUDIO_BOT)
public class AudioBotProductEditRequest extends AbstractProductEditRequest {
    private Integer providerNodeId;
}
