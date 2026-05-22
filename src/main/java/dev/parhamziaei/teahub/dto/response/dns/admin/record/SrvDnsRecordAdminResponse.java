package dev.parhamziaei.teahub.dto.response.dns.admin.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class SrvDnsRecordAdminResponse extends AbstractDnsRecordResponse {

    private String host;
    private Integer port;
    private Integer priority;
    private Integer weight;
    private Long ownerId;
    private Long targetResourceId;

}
