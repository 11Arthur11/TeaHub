package dev.parhamziaei.teahub.entity.jpa.dns;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
public class SrvRecord extends DnsRecord {

    private String host;
    private Integer port;
    private Integer priority;
    private Integer weight;

}
