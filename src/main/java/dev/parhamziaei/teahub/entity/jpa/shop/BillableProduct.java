package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

@Entity
@DiscriminatorValue("BILLABLE_PRODUCT")
@NoArgsConstructor
public abstract class BillableProduct extends BaseProduct {

    private Duration expiration;

    public BillableProduct(
            String productName,
            Category category,
            Money price,
            boolean enabled,
            Duration expiration
    ) {
        super(productName, category, price, enabled);
        this.expiration = expiration;
    }

}
