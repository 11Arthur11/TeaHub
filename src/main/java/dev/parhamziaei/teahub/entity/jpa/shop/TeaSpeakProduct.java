package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("TEASPEAK_PRODUCT")
@NoArgsConstructor
public class TeaSpeakProduct extends BillableProduct {

    private Integer maxClients;

    @Builder
    public TeaSpeakProduct(
            String productName,
            Category category,
            Money price,
            boolean enabled,
            Duration expiration,
            Integer maxClients
    ) {
        super(productName, category, price, enabled, expiration);
        this.maxClients = maxClients;
    }

}
