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

    private Long id;

    private String name;

    private String server;

    private AudioBotStatus status;

}
