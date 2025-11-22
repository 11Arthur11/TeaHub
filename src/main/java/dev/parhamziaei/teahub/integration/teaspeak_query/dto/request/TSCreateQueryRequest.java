package dev.parhamziaei.teahub.integration.teaspeak_query.dto.request;

import lombok.Data;

@Data
public class TSCreateQueryRequest {

    String serverName;
    String port;
    String maxClients;

}
