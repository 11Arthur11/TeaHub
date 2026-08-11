package dev.parhamziaei.teahub.integration.audio_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ABConnectSettingsResponse {

    private String address;
    private String channel;
    private String badges;
    private String name;

    private ServerPassword serverPassword;

    @Data
    public static class ServerPassword {
        @JsonProperty("pw")
        private String password;

        private boolean hashed;

        @JsonProperty("autohash")
        private boolean autoHash;
    }

}
