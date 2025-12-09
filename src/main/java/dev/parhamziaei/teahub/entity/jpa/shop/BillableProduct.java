package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;

@Entity
@Setter
@Getter
@DiscriminatorValue("BILLABLE_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BillableProduct extends BaseProduct {

    private Duration expiration;

}