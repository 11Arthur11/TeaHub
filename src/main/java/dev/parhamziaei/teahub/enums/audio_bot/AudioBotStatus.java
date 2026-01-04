package dev.parhamziaei.teahub.enums.audio_bot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;

import java.util.Arrays;

public enum AudioBotStatus {

    OFFLINE(0, "status.offline"),
    CONNECTING(1, "status.connecting"),
    CONNECTED(2, "status.online"),;

    private final String key;
    private final int code;

    AudioBotStatus(int code, String key) {
        this.key = key;
        this.code = code;
    }

    public String key() {
        return this.key;
    }

    @JsonValue
    public int code() {
        return code;
    }

    public static TeaSpeakStatus fromValue(String value) {
        return Arrays.stream(TeaSpeakStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(TeaSpeakStatus.OFFLINE);
    }

    @JsonCreator
    public static AudioBotStatus fromCode(int code) {
        for (AudioBotStatus a : AudioBotStatus.values()) {
            if (a.code == code) {
                return a;
            }
        }
        throw new IllegalArgumentException("Invalid AudioBotStatus code: " + code);
    }

}
