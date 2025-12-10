package dev.parhamziaei.teahub.entity.jpa.resource;

import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.TeaSpeakResourceToken;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    private String sid;

    @OneToMany(mappedBy = "teaSpeakResource", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TeaSpeakResourceToken> privilegeTokens;

    public void setParentQueryInstance(QueryInstance parentQueryInstance) {
        this.parentQueryInstance = parentQueryInstance;
        if (this.parentQueryInstance.getInstances() == null)
            this.parentQueryInstance.setInstances(new ArrayList<>());
        this.parentQueryInstance.getInstances().add(this);
    }

    public void addPrivilegeToken(TeaSpeakResourceToken privilegeToken) {
        if (this.privilegeTokens == null)
            this.privilegeTokens = new ArrayList<>();
        privilegeToken.setTeaSpeakResource(this);
        this.privilegeTokens.add(privilegeToken);
    }

}
