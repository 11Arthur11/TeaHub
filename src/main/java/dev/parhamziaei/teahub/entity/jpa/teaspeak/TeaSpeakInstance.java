package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class TeaSpeakInstance extends BaseEntity<Long> {

    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User owner;

    private Integer maxClients;

    private Duration expiration;

    @Embedded
    private Money price;

}
