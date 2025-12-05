package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource")
@DiscriminatorColumn(name = "resource_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BaseResource extends BaseEntity<Long> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private BaseProduct product;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime orderDate;

    protected BaseResource() {}

    public BaseResource(BaseProduct product, String label, User owner) {
        this.product = product;
        this.label = label;
        this.owner = owner;
        this.orderDate = LocalDateTime.now().withNano(0);
    }

}
