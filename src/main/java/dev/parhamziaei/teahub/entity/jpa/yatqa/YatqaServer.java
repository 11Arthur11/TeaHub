package dev.parhamziaei.teahub.entity.jpa.yatqa;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.integration.yatqa.model.ServerQueryCredentials;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YatqaServer extends BaseEntity<Long> {

    @Embedded
    private ServerQueryCredentials credentials;

    private String status;

    private Integer maxVM;

    private String portRange;

}
