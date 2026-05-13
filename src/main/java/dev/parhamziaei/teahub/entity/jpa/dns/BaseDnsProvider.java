package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
public class BaseDnsProvider extends BaseEntity<Long> {

    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", updatable = false, insertable = false)
    private DnsProviderType provider;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "provider")
    private List<Zone> zones;

}