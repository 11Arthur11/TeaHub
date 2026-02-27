package dev.parhamziaei.teahub.entity.jpa.system;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SystemNotification extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    private User publisher;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(columnDefinition = "TIMESTAMP(0)", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiresAt;

}
