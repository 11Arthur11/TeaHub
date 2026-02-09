package dev.parhamziaei.teahub.integration.audio_bot.dto.playlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABPlayListDetailResponse extends ABPlayListsResponse {

    @JsonProperty("Items")
    List<ABPlayListItemResponse> playListItems;

    public List<ABPlayListItemResponse> getPlayListItems() {
        if (playListItems == null)
            playListItems = new ArrayList<>();
        return playListItems;
    }

}
