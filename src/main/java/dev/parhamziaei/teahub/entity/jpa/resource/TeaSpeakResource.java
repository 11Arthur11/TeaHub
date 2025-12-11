package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.TeaSpeakStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "query_instance_id")
    private QueryInstance parentQueryInstance;

    private TeaSpeakStatus teaSpeakStatus;

    private String sid;

    @OneToOne(mappedBy = "teaSpeakResource", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "privilege_token_id")
    private TeaSpeakResourceToken privilegeTokens;

    public void setParentQueryInstance(QueryInstance parentQueryInstance) {
        this.parentQueryInstance = parentQueryInstance;
        if (this.parentQueryInstance.getInstances() == null)
            this.parentQueryInstance.setInstances(new ArrayList<>());
        this.parentQueryInstance.getInstances().add(this);
    }

    public void setPrivilegeToken(TeaSpeakResourceToken privilegeToken) {
        this.privilegeTokens = privilegeToken;
        privilegeToken.setTeaSpeakResource(this);
    }

}
