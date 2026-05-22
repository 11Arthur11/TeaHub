package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class ABPlayListsResponseMixin {

    @JsonProperty("Id")
    abstract String getPlaylistFilename();

    @JsonProperty("Title")
    abstract String getTitle();

    @JsonProperty("SongCount")
    abstract int getSongCount();

    @JsonProperty("DisplayOffset")
    abstract int getDisplayOffset();

}
