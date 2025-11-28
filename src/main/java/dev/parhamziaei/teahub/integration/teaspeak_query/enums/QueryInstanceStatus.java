package dev.parhamziaei.teahub.integration.teaspeak_query.enums;

import dev.parhamziaei.teahub.enums.TicketStatus;

import java.util.Arrays;

public enum QueryInstanceStatus {
    DISABLED("query-instance.status.disabled"),
    FULL("query-instance.status.full"),
    UNREACHABLE("query-instance.status.unreachable"),
    LOGIN_FAILED("query-instance.status.login-failed"),
    DISPATCHED("query-instance.status.dispatched"),
    INITIALIZING("query-instance.status.initializing");

    private final String key;

    QueryInstanceStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    public static TicketStatus fromValue(String value) {
        return Arrays.stream(TicketStatus.values())
                .filter(v -> v.value().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Yatqa status value"));
    }
}
