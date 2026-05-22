package dev.parhamziaei.teahub.integration.audio_bot.dto.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class ABPlayListDetailResponseMixin extends ABPlayListsResponseMixin {

    @JsonProperty("Items")
    abstract List<ABPlayListItemResponseMixin> getPlayListItems();

}
