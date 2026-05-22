package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.DnsProviderStatus;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "dns_provider")
@DiscriminatorColumn(name = "provider_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseDnsProvider {

    @Id
    private Long id;

    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", updatable = false, insertable = false)
    private DnsProviderType type;

    @Enumerated(EnumType.STRING)
    private DnsProviderStatus status = DnsProviderStatus.UNKNOWN;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "provider")
    private List<DnsZone> dnsZones = new ArrayList<>();

    public BaseDnsProvider(DnsProviderType type, Long id) {
        this.id = id;
        this.type = type;
    }

}