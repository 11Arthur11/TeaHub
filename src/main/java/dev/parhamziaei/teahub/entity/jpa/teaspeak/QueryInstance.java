package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.QueryInstanceStatus;
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
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"ip", "port"})
)
public class QueryInstance extends BaseEntity<Long> {

    private String name;
    
    @Embedded
    private ServerQueryCredentials credentials;

    @Enumerated(EnumType.STRING)
    private QueryInstanceStatus status;

    private Integer maxTeaSpeakInstance;

    private Integer startPort;

    private Integer stopPort;

    private boolean isFull;

    @Column(columnDefinition = "TIMESTAMP(0)", updatable = false)
    private LocalDateTime initiatedAt;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime lastUsed;

    @OneToMany(mappedBy = "parentQueryInstance", fetch = FetchType.LAZY)
    private List<TeaSpeakResource> instances = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        this.lastUsed = LocalDateTime.now().withNano(0);
        if (instances.size() >= maxTeaSpeakInstance) {
            this.isFull = true;
            this.status = QueryInstanceStatus.FULL;
        }
    }

    @PrePersist
    public void prePersist() {
        this.initiatedAt = LocalDateTime.now().withNano(0);
        this.lastUsed = LocalDateTime.now().withNano(0);
        this.isFull = instances.size() >= maxTeaSpeakInstance;
    }

    public String getAddress() {
        return this.credentials.ip() + ":" + this.credentials.port();
    }
}
