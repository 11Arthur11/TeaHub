package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.BaseResource;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "product")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseProduct extends BaseEntity<Long> {

    private String productName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy ="product", fetch = FetchType.LAZY)
    private List<BaseResource> UserResources;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    private boolean enabled;

    public BaseProduct(String productName, Category category, Money price, boolean enabled) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.enabled = enabled;
        this.UserResources = new ArrayList<>();
    }

}
