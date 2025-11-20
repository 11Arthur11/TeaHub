package dev.parhamziaei.teahub.integration.yatqa.enums;

import dev.parhamziaei.teahub.enums.TicketStatus;

import java.util.Arrays;

public enum YatqaStatus {
    READY("ticket.status.ready", "ready"),
    FULL("ticket.status.full", "full"),
    UNREACHABLE("ticket.status.unreachable", "unreachable"),
    DISPATCHED("ticket.status.dispatched", "dispatched");

    private final String key;
    private final String value;

    YatqaStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }
    public String value() {
        return value;
    }

    public static TicketStatus fromValue(String value) {
        return Arrays.stream(TicketStatus.values())
                .filter(v -> v.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Yatqa status value"));
    }
}
