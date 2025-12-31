package dev.parhamziaei.teahub.enums.audio_bot;

import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;

import java.util.Arrays;

public enum AudioBotStatus {

    ONLINE("status.online"),
    OFFLINE("status.offline"),;

    private final String key;
    AudioBotStatus(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }
    public static TeaSpeakStatus fromValue(String value) {
        return Arrays.stream(TeaSpeakStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(TeaSpeakStatus.OFFLINE);
    }

}
