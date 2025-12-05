package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.InstanceStatus;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("TEASPEAK_RESOURCE")
public class TeaSpeakResource extends BillableResource {

    private Integer maxClients;

    private Integer port;

    private InstanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_instance_id")
    private QueryInstance parentQueryInstance;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2)),
//            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
//    })
//    private Money price;

    @Builder
    public TeaSpeakResource(
            BaseProduct product,
            String label,
            User owner,
            Integer maxClients,
            LocalDateTime expiration,
            InstanceStatus status
    ) {
        super(product, label, owner, expiration);
        this.maxClients = maxClients;
        this.status = status;
    }

}
