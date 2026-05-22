package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABConnectSettingsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class ABConnectSettingsResponseMixin {

    @JsonProperty("server_password")
    abstract ABConnectSettingsResponse.ServerPassword getServerPassword();

}
