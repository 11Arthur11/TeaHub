package dev.parhamziaei.teahub.integration.audio_bot.dto.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABPlayListItemResponse {

    @JsonProperty("Link")
    private String link;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("AudioType")
    private String audioType;
}
