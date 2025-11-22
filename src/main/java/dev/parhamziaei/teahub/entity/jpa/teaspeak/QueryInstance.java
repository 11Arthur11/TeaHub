package dev.parhamziaei.teahub.entity.jpa.teaspeak;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.QueryInstanceStatus;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryInstance extends BaseEntity<Long> {

    private String name;
    
    @Embedded
    private ServerQueryCredentials credentials;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private QueryInstanceStatus status;

    private Integer maxVM;

    private Integer startPort;

    private Integer endPort;

    @OneToMany(mappedBy = "parentQueryInstance", fetch = FetchType.LAZY)
    private List<TeaSpeakInstance> instances;

}
