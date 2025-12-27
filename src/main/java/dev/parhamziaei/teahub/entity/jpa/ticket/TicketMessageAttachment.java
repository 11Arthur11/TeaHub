package dev.parhamziaei.teahub.entity.jpa.ticket;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketMessageAttachment extends BaseEntity<Long> {

    @Column(name = "original_name")
    private String originalName;

    private String storedName;

    private String storedPath;

    private String mimeType;

    private Long size;

    @Column(columnDefinition = "TIMESTAMP(0)", name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_message_id")
    private TicketMessage ticketMessage;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now().withNano(0);
    }

}
