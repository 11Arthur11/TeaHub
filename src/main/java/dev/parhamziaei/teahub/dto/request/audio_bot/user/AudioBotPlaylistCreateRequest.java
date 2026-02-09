package dev.parhamziaei.teahub.dto.request.audio_bot.user;

import dev.parhamziaei.teahub.validation.annotation.SafeName;
import lombok.Data;

@Data
public class AudioBotPlaylistCreateRequest {

    @SafeName
    private String playlistName;

}
