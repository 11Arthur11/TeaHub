package dev.parhamziaei.teahub.dto.response.dashboard.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TicketMetric {

    private Long pending;
    private Long waiting;
    private Long closed;
    private Long responded;

    public TicketMetric(Long pending, Long waiting, Long closed, Long responded) {
        this.pending = pending;
        this.waiting = waiting;
        this.closed = closed;
        this.responded = responded;
    }

    public TicketMetric() {}

}