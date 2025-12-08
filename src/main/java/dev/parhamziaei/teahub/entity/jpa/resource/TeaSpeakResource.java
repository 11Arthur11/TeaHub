package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("TEASPEAK_RESOURCE")
@SuperBuilder
public class TeaSpeakResource extends BillableResource {

    private Integer maxClients;

    private Integer port;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_instance_id")
    private QueryInstance parentQueryInstance;

    @Column(unique = true)
    private String sid;

    private String privilegeToken;

    public void setParentQueryInstance(QueryInstance parentQueryInstance) {
        this.parentQueryInstance = parentQueryInstance;
        if (this.parentQueryInstance.getInstances() == null)
            this.parentQueryInstance.setInstances(new ArrayList<>());
        this.parentQueryInstance.getInstances().add(this);
    }

}
