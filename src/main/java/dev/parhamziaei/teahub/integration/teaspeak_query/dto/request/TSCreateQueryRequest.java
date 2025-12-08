package dev.parhamziaei.teahub.integration.teaspeak_query.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TSCreateQueryRequest {

    String serverName;
    String port;
    String maxClients;

}
