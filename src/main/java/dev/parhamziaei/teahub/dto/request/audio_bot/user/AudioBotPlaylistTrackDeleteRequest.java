package dev.parhamziaei.teahub.dto.request.audio_bot.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioBotPlaylistTrackDeleteRequest {

    private Integer trackIndex;

}
