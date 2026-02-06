package dev.parhamziaei.teahub.dto.response.audio_bot.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AudioBotPlayListsUserResponse {

    private String title;

    private Integer songCount;

}
