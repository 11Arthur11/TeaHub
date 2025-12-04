package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@DiscriminatorValue("TEASPEAK_PRODUCT")
@NoArgsConstructor
public class TeaSpeakProduct extends BaseProduct {

    private Integer maxClients;

    private Duration expiration;

    @Builder
    public TeaSpeakProduct(
            String productName,
            Category category,
            Money price,
            Integer maxClients,
            Duration expiration
    ) {
        super(productName, category, price);
        this.maxClients = maxClients;
        this.expiration = expiration;
    }

}
