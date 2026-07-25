package dev.parhamziaei.teahub.dto.response.resource.audio_bot.user;

import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AudioBotResourceDetailResponse extends AbstractResourceDetailResponse {

    private String botNickname;

    private String serverAddress;

    private String serverPassword;

    private AudioBotStatus botStatus;

}
