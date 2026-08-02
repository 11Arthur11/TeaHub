package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "dns_record")
@DiscriminatorColumn(name = "record_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DnsRecord extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", updatable = false, insertable = false)
    private DnsRecordType recordType;

    @ManyToOne(fetch = FetchType.EAGER)
    private DnsZone dnsZone;

    public String getNameWithoutTs3Prefix() {
        return name.replace("_ts3._udp.", "");
    }

}
