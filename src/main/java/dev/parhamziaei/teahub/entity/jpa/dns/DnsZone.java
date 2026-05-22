package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.ZoneStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DnsZone extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    private boolean active = false;

    @Enumerated(EnumType.STRING)
    private ZoneStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private BaseDnsProvider provider;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dnsZone")
    private List<DnsRecord> records = new ArrayList<>();

    public DnsZone(String name, ZoneStatus status, BaseDnsProvider provider) {
        this.name = name;
        this.status = status;
        this.provider = provider;
    }

}
