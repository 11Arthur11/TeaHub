package dev.parhamziaei.teahub.dto.response.resource.audio_bot.user;

import dev.parhamziaei.teahub.integration.audio_bot.dto.ABApiTokenResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudioBotScopedPanelAccessResponse {

    private String panelAddress;
    private ABApiTokenResponse token;

}
