package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.dto.request.ResourceRequestSubType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ResourceRequestSubType(ResourceType.AUDIO_BOT)
public class AudioBotProductInitRequest extends AbstractProductInitRequest {
    private Long providerNodeId;
}
