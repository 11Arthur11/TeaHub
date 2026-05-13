package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.ZoneStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Zone extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    private ZoneStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private BaseDnsProvider provider;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "zone")
    private List<DnsRecord> records = new ArrayList<>();

}
