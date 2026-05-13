package dev.parhamziaei.teahub.entity.jpa.dns;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResourceToken;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("LIARA")
@SuperBuilder
public class LiaraDnsProvider extends BaseDnsProvider {

    private String baseUrl;
    private String apiKey;

}
