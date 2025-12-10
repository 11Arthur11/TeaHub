package dev.parhamziaei.teahub.integration.teaspeak_query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TSCreateQueryRequest {

    String serverName;
    String port;
    String maxClients;

}
