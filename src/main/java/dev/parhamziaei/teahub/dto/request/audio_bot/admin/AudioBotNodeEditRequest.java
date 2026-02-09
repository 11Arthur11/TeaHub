package dev.parhamziaei.teahub.dto.request.audio_bot.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AudioBotNodeEditRequest {

    private String name;

    private String username;

    private String password;

    private Integer maxBotInstance;

    private boolean enabled;

}
