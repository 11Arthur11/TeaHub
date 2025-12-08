package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("TEASPEAK_PRODUCT")
@NoArgsConstructor
@SuperBuilder
public class TeaSpeakProduct extends BillableProduct {

    private Integer maxClients;

}
