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
@DiscriminatorValue("A")
@SuperBuilder
public class ADnsRecord extends DnsRecord {

    private String ip;

}
