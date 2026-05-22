package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class ABInstanceListResponseMixin {

    @JsonProperty("Id")
    abstract Long getId();

    @JsonProperty("Name")
    abstract String getName();

    @JsonProperty("Server")
    abstract String getServer();

    @JsonProperty("Status")
    abstract AudioBotStatus getStatus();

}
