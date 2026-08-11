package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public abstract class ABApiTokenResponseMixin {

    @JsonProperty("Credential")
    abstract String getCredentials();
    @JsonProperty("ValidUntil")
    abstract LocalDateTime getValidUntil();

}
