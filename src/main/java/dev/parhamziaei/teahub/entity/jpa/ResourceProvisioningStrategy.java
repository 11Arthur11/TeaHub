package dev.parhamziaei.teahub.entity.jpa;

import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResourceProvisioningStrategy {

    @Id
    @Enumerated(EnumType.STRING)
    private ResourceType nodeType;

    private ProvisionStrategy strategy;

}
