package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("SRV")
@SuperBuilder
public class SrvDnsRecord extends DnsRecord {

    private String host;
    private Integer port;
    private Integer priority;
    private Integer weight;
    private Integer ttl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_resource_id")
    private TeaSpeakResource targetResource;

    private boolean assigned = false;

    @PrePersist
    public void prePersist() {
        this.setAssigned(this.owner != null && this.targetResource != null);
    }

    public boolean hasTargetResource() {
        return this.targetResource != null;
    }

}
