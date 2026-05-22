package dev.parhamziaei.teahub.integration.audio_bot.dto.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABPlayListsResponse {

    private String playlistFilename;

    private String title;

    private int songCount;

    private int displayOffset;

}
