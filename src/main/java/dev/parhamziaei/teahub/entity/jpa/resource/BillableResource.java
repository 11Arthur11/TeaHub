package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("BILLABLE_RESOURCE")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BillableResource extends BaseResource {

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime expiration;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status;

    private boolean autoProlong = true;

}
