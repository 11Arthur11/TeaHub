package dev.parhamziaei.teahub.integration.audio_bot.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ABPlayListItemResponse {

    private Integer index;

    private String link;

    private String title;

    private String audioType;
}
