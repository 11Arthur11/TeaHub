package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "resource")
@DiscriminatorColumn(name = "resource_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseResource extends BaseEntity<Long> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private BaseProduct product;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime orderDate;

    public BaseResource(
            BaseProduct product,
            String label,
            User owner
    ) {
        this.product = product;
        this.label = label;
        this.owner = owner;
    }

}
