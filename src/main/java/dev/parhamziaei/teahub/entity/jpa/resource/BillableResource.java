package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("BILLABLE_RESOURCE")
@NoArgsConstructor
public abstract class BillableResource extends BaseResource {

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiration;

    private boolean autoProlong = true;

    public BillableResource(BaseProduct product, String label, User owner, LocalDateTime expiration) {
        super(product, label, owner);
        this.expiration = expiration;
    }

}
