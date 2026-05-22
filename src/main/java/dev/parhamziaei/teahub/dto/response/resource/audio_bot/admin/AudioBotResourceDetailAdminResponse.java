package dev.parhamziaei.teahub.dto.response.resource.audio_bot.admin;

import dev.parhamziaei.teahub.dto.response.resource.audio_bot.user.AudioBotResourceDetailResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class AudioBotResourceDetailAdminResponse extends AudioBotResourceDetailResponse {
    private String identifier;
    private Long ownerId;
}
