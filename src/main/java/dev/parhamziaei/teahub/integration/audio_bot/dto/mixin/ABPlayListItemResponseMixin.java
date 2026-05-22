package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public abstract class ABPlayListItemResponseMixin {

    @JsonProperty("Link")
    abstract String getLink();

    @JsonProperty("Title")
    abstract String getTitle();

    @JsonProperty("AudioType")
    abstract String getAudioType();

}
