package dev.parhamziaei.teahub.integration.audio_bot.dto.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABPlayListsResponse {

    @JsonProperty("Id")
    private String playlistFilename;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("SongCount")
    private int songCount;

    @JsonProperty("DisplayOffset")
    private int displayOffset;

}
