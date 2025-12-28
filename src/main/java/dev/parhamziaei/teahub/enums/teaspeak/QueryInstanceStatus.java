package dev.parhamziaei.teahub.enums.teaspeak;

import dev.parhamziaei.teahub.enums.ticket.TicketStatus;

import java.util.Arrays;

public enum QueryInstanceStatus {
    DISABLED("query-instance.status.disabled"),
    FULL("query-instance.status.full"),
    UNREACHABLE("query-instance.status.unreachable"),
    RECONNECTING("query-instance.status.reconnecting"),
    LOGIN_FAILED("query-instance.status.login-failed"),
    DISPATCHED("query-instance.status.dispatched"),
    INITIATED("query-instance.status.initiated");

    private final String key;

    QueryInstanceStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
