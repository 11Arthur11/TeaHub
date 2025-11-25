package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstance extends BaseEntity<Long> {

    private String name;
    
    @Embedded
    private ServerQueryCredentials credentials;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private QueryInstanceStatus status;

    private Integer maxTeaSpeakInstance;

    private Integer startPort;

    private Integer stopPort;

    private boolean isFull;

    @Column(columnDefinition = "TIMESTAMP(0)", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lastUsed;

    @OneToMany(mappedBy = "parentQueryInstance", fetch = FetchType.LAZY)
    private List<TeaSpeakInstance> instances = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.lastUsed = LocalDateTime.now().withNano(0);
        this.isFull = instances.size() >= maxTeaSpeakInstance;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().withNano(0);
        this.lastUsed = LocalDateTime.now().withNano(0);
    }
}
