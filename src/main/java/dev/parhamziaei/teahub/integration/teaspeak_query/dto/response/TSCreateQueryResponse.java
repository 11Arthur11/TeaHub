package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TSCreateQueryResponse extends BaseQueryResponse {
    private String virtualserver_port;
    private String virtualserver_maxclients;
    private String sid;
    private String token;
}
