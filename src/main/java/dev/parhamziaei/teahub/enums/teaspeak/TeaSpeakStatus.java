package dev.parhamziaei.teahub.enums.teaspeak;

import java.util.Arrays;

public enum TeaSpeakStatus {
    ONLINE("teaspeak-status.online"),
    OFFLINE("teaspeak-status.offline"),;

    private final String key;
    TeaSpeakStatus(String key) {
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
