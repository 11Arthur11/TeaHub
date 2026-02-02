package dev.parhamziaei.teahub.dto.request.audio_bot.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AudioBotNodeInitRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    @Schema(description = "The web address should not end with /", examples = "http://music-bot.example.com:45855")
    private String webAddress;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private Integer maxBotInstance;

    private boolean enabled;

}
