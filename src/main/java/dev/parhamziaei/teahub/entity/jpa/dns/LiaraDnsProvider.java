package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@DiscriminatorValue("LIARA")
@SuperBuilder
public class LiaraDnsProvider extends BaseDnsProvider {

    public static Long STATIC_ID = 1L;

    private String baseUrl;
    private String apiKey;

    public LiaraDnsProvider() {
        super(DnsProviderType.LIARA, STATIC_ID);
    }

}
