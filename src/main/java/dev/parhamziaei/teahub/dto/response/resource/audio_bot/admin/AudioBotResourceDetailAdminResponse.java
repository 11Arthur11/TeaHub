package dev.parhamziaei.teahub.dto.response.resource.audio_bot.admin;

import dev.parhamziaei.teahub.dto.response.resource.audio_bot.user.AudioBotResourceDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AudioBotResourceDetailAdminResponse extends AudioBotResourceDetailResponse {
    private String ownerPhone;
}
