package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("BILLABLE")
@NoArgsConstructor
public class BillableResource extends BaseResource {

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiration;

    private boolean autoProlong = true;

    public BillableResource(BaseProduct product, String label, User owner, LocalDateTime expiration) {
        super(product, label, owner);
        this.expiration = expiration;
    }

}
