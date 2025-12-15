package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "resource")
@DiscriminatorColumn(name = "resource_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BillableResource extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private BillableProduct product;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", updatable = false, insertable = false)
    private ResourceType resourceType;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime orderDate;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiration;

    @Enumerated(EnumType.STRING)
    private ResourceStatus resourceStatus;

    private boolean autoProlong = true;

}
