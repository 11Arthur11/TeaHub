package dev.parhamziaei.teahub.integration.audio_bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABInstanceListResponse {

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Server")
    private String server;

    @JsonProperty("Status")
    private AudioBotStatus status;

}
