package dev.parhamziaei.teahub.dto.response.dns.admin.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
@Builder
public class SrvDnsRecordAdminResponse extends AbstractDnsRecordResponse {

    private String host;
    private Integer port;
    private Integer priority;
    private Integer weight;
    private Integer ttl;
    private Long ownerId;
    private Long targetResourceId;
    private boolean assigned;

}
