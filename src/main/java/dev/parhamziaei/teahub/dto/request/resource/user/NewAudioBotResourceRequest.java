package dev.parhamziaei.teahub.dto.request.resource.user;

import dev.parhamziaei.teahub.dto.request.ResourceRequestSubType;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ResourceRequestSubType(ResourceType.AUDIO_BOT)
@Data
public class NewAudioBotResourceRequest extends AbstractNewResourceRequest {

    private String botNickname;

    private String serverAddress;

    private String serverPassword;

}
