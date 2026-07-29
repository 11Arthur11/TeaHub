package dev.parhamziaei.teahub.entity.jpa.shop;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.valueobject.Money;
import dev.parhamziaei.teahub.valueobject.ProductPresentation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "product")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
@SuperBuilder
public class BillableProduct extends BaseEntity<Long> {

    private String productName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy ="product", fetch = FetchType.LAZY)
    private List<BillableResource> UserResources;

    private ProductPresentation presentation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private ProductPeriod period;

    @Column(name = "product_type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private Duration expiration;

    public void setCategory(Category category) {
        Hibernate.initialize(category.getProducts());
        if (this.category != null)
            this.category.getProducts().remove(this);
        this.category = category;
        category.getProducts().add(this);
    }

    public void addUserResource(BillableResource resource) {
        if (this.UserResources == null)
            this.UserResources = new ArrayList<>();
        resource.setProduct(this);
        this.UserResources.add(resource);
    }

}
