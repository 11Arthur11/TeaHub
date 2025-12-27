package dev.parhamziaei.teahub.enums.ticket;

import java.util.Arrays;

public enum TicketStatus {

    PENDING("ticket.status.pending"),
    CLOSED("ticket.status.closed"),
    RESPONDED("ticket.status.responded"),
    WAITING("ticket.status.waiting");

    private final String key;

    TicketStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
