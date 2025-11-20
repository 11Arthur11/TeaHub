package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;

@MappedSuperclass
public class BaseProduct extends BaseEntity<Long> {

    private String productName;
    private Category category;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;




}
