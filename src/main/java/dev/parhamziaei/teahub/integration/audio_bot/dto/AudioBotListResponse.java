package dev.parhamziaei.teahub.integration.audio_bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;

public class AudioBotListResponse {

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Server")
    private String server;

    @JsonProperty("Status")
    private AudioBotStatus status;

}
