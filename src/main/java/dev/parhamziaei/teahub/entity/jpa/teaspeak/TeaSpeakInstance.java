package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TeaSpeakInstance extends BaseEntity<Long> {

    private String productName;

    private String instanceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    private Integer maxClients;

    private Integer port;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiration;

    private boolean autoProlong = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_instance_id")
    private QueryInstance parentQueryInstance;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 19, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

}
