package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;

@Entity
@Setter
@Getter
@NoArgsConstructor
@DiscriminatorValue("TEASPEAK")
@SuperBuilder
public class TeaSpeakResource extends BillableResource {

    private Integer maxClients;

    private Integer port;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "query_instance_id")
    private QueryInstance parentQueryInstance;

    @Enumerated(EnumType.STRING)
    private TeaSpeakStatus teaSpeakStatus;

    private String sid;

    @OneToOne(mappedBy = "teaSpeakResource", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TeaSpeakResourceToken privilegeToken;

    public void setParentQueryInstance(QueryInstance parentQueryInstance) {
        this.parentQueryInstance = parentQueryInstance;
        if (this.parentQueryInstance.getInstances() == null)
            this.parentQueryInstance.setInstances(new ArrayList<>());
        this.parentQueryInstance.getInstances().add(this);
    }

    public void setPrivilegeToken(TeaSpeakResourceToken privilegeToken) {
        privilegeToken.setTeaSpeakResource(this);
        this.privilegeToken = privilegeToken;
    }

}
