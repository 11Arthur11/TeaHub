package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class TSCreateQueryResponse extends BaseQueryResponse {
    String virtualserver_port;
    String virtualserver_maxclients;
    String sid;
    String token;
}
